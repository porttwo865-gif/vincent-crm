package cn.vincent.crm.approval.service;

import cn.vincent.common.exception.GenericException;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.approval.domain.ApprovalInstance;
import cn.vincent.crm.approval.domain.ApprovalNodeRecord;
import cn.vincent.crm.approval.domain.ApprovalTemplate;
import cn.vincent.crm.approval.dto.request.ApprovalCancelRequest;
import cn.vincent.crm.approval.dto.request.ApprovalInstancePageRequest;
import cn.vincent.crm.approval.dto.request.ApprovalOperateRequest;
import cn.vincent.crm.approval.dto.request.ApprovalSubmitRequest;
import cn.vincent.crm.approval.dto.response.ApprovalInstanceGetResponse;
import cn.vincent.crm.approval.dto.response.ApprovalInstanceListResponse;
import cn.vincent.crm.approval.dto.response.ApprovalNodeRecordResponse;
import cn.vincent.crm.approval.mapper.ApprovalInstanceMapper;
import cn.vincent.crm.approval.mapper.ApprovalNodeRecordMapper;
import cn.vincent.crm.approval.mapper.ExtApprovalInstanceMapper;
import cn.vincent.crm.approval.mapper.ExtApprovalNodeRecordMapper;
import cn.vincent.crm.system.domain.User;
import cn.vincent.crm.system.mapper.UserMapper;
import cn.vincent.crm.system.service.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 审批实例服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ApprovalInstanceService {

    @Resource
    private ApprovalInstanceMapper approvalInstanceMapper;

    @Resource
    private ExtApprovalInstanceMapper extApprovalInstanceMapper;

    @Resource
    private ApprovalNodeRecordMapper approvalNodeRecordMapper;

    @Resource
    private ExtApprovalNodeRecordMapper extApprovalNodeRecordMapper;

    @Resource
    private ApprovalTemplateService approvalTemplateService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private BaseService baseService;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 发起审批
     * <p>
     * 根据 bizType 查找 enabled 的模板 → 创建 instance(status=pending, currentNodeSeq=1)
     * → 为第一个节点的所有 approverIds 创建 NodeRecord(status=pending)
     *
     * @param request 发起审批请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 审批实例
     */
    public ApprovalInstance submit(ApprovalSubmitRequest request, String userId, String orgId) {
        // 根据 bizType 查找启用的模板
        List<ApprovalTemplate> templates = approvalTemplateService.getEnabledByBizType(request.getBizType(), orgId);
        if (templates == null || templates.isEmpty()) {
            throw new GenericException(Translator.get("approval.template.not.found"));
        }
        ApprovalTemplate template = templates.get(0);

        // 解析节点配置
        List<Map<String, Object>> nodes = parseNodes(template.getNodes());
        if (nodes == null || nodes.isEmpty()) {
            throw new GenericException(Translator.get("approval.template.nodes.empty"));
        }

        long now = System.currentTimeMillis();

        // 创建审批实例
        ApprovalInstance instance = new ApprovalInstance();
        instance.setId(IDGenerator.nextStr());
        instance.setTemplateId(template.getId());
        instance.setTemplateName(template.getName());
        instance.setBizType(request.getBizType());
        instance.setBizId(request.getBizId());
        instance.setBizName(request.getBizName());
        instance.setApplicant(userId);
        instance.setStatus("pending");
        instance.setCurrentNodeSeq(1);
        instance.setRemark(request.getRemark());
        instance.setOrganizationId(orgId);
        instance.setCreateUser(userId);
        instance.setUpdateUser(userId);
        instance.setCreateTime(now);
        instance.setUpdateTime(now);
        approvalInstanceMapper.insert(instance);

        // 为第一个节点创建审批记录
        Map<String, Object> firstNode = nodes.get(0);
        @SuppressWarnings("unchecked")
        List<String> approverIds = (List<String>) firstNode.get("approverIds");
        if (approverIds != null && !approverIds.isEmpty()) {
            List<ApprovalNodeRecord> records = new ArrayList<>();
            for (String approverId : approverIds) {
                ApprovalNodeRecord record = new ApprovalNodeRecord();
                record.setId(IDGenerator.nextStr());
                record.setInstanceId(instance.getId());
                record.setNodeSeq(1);
                record.setApproverId(approverId);
                record.setStatus("pending");
                record.setCreateUser(userId);
                record.setUpdateUser(userId);
                record.setCreateTime(now);
                record.setUpdateTime(now);
                records.add(record);
            }
            approvalNodeRecordMapper.batchInsert(records);
        }

        return instance;
    }

    /**
     * 撤回审批
     * <p>
     * 仅申请人可操作，仅 pending 状态
     *
     * @param request 撤回请求
     * @param userId  当前用户 ID
     */
    public void cancel(ApprovalCancelRequest request, String userId) {
        ApprovalInstance instance = approvalInstanceMapper.selectByPrimaryKey(request.getInstanceId());
        if (instance == null) {
            throw new GenericException(Translator.get("approval.instance.not.exist"));
        }

        // 仅申请人可撤回
        if (!StringUtils.equals(instance.getApplicant(), userId)) {
            throw new GenericException(Translator.get("approval.cancel.only.applicant"));
        }

        // 仅 pending 状态可撤回
        if (!StringUtils.equals(instance.getStatus(), "pending")) {
            throw new GenericException(Translator.get("approval.cancel.only.pending"));
        }

        instance.setStatus("cancelled");
        instance.setUpdateUser(userId);
        instance.setUpdateTime(System.currentTimeMillis());
        approvalInstanceMapper.update(instance);
    }

    /**
     * 审批通过
     * <p>
     * 更新当前 NodeRecord → 检查当前节点是否满足通过条件
     * (AND:全部通过 / OR:任一通过) → 若满足则 currentNodeSeq++
     * → 若无下一节点则 instance.status=approved → 若有则为下一节点创建 NodeRecord
     *
     * @param request 审批操作请求
     * @param userId  当前用户 ID
     */
    public void approve(ApprovalOperateRequest request, String userId) {
        ApprovalInstance instance = approvalInstanceMapper.selectByPrimaryKey(request.getInstanceId());
        if (instance == null) {
            throw new GenericException(Translator.get("approval.instance.not.exist"));
        }

        if (!StringUtils.equals(instance.getStatus(), "pending")) {
            throw new GenericException(Translator.get("approval.instance.not.pending"));
        }

        // 查找当前用户在当前节点的待审批记录
        ApprovalNodeRecord currentRecord = findPendingRecord(instance.getId(), instance.getCurrentNodeSeq(), userId);
        if (currentRecord == null) {
            throw new GenericException(Translator.get("approval.node.not.pending"));
        }

        long now = System.currentTimeMillis();

        // 更新当前节点记录为通过
        currentRecord.setStatus("approved");
        currentRecord.setComment(request.getComment());
        currentRecord.setOperateTime(now);
        currentRecord.setUpdateUser(userId);
        currentRecord.setUpdateTime(now);
        approvalNodeRecordMapper.update(currentRecord);

        // 查询模板节点配置
        ApprovalTemplate template = approvalTemplateService.getEnabledByBizType(instance.getBizType(), instance.getOrganizationId())
                .stream()
                .filter(t -> StringUtils.equals(t.getId(), instance.getTemplateId()))
                .findFirst()
                .orElse(null);
        if (template == null) {
            throw new GenericException(Translator.get("approval.template.not.exist"));
        }

        List<Map<String, Object>> nodes = parseNodes(template.getNodes());
        Map<String, Object> currentNodeConfig = nodes.get(instance.getCurrentNodeSeq() - 1);
        String nodeType = (String) currentNodeConfig.get("type");

        // 检查当前节点是否满足通过条件
        boolean nodePassed = false;
        if ("AND".equals(nodeType)) {
            // 会签：所有人通过才算通过
            List<ApprovalNodeRecord> pendingRecords = extApprovalNodeRecordMapper
                    .selectPendingByInstanceAndSeq(instance.getId(), instance.getCurrentNodeSeq());
            nodePassed = pendingRecords.isEmpty();
        } else if ("OR".equals(nodeType)) {
            // 或签：任一人通过即通过
            nodePassed = true;
        }

        if (nodePassed) {
            // 推进到下一节点
            int nextNodeSeq = instance.getCurrentNodeSeq() + 1;
            if (nextNodeSeq > nodes.size()) {
                // 无下一节点，审批通过
                instance.setStatus("approved");
                instance.setUpdateUser(userId);
                instance.setUpdateTime(now);
                approvalInstanceMapper.update(instance);
            } else {
                // 有下一节点，推进并创建记录
                instance.setCurrentNodeSeq(nextNodeSeq);
                instance.setUpdateUser(userId);
                instance.setUpdateTime(now);
                approvalInstanceMapper.update(instance);

                @SuppressWarnings("unchecked")
                List<String> nextApproverIds = (List<String>) nodes.get(nextNodeSeq - 1).get("approverIds");
                if (nextApproverIds != null && !nextApproverIds.isEmpty()) {
                    List<ApprovalNodeRecord> newRecords = new ArrayList<>();
                    for (String approverId : nextApproverIds) {
                        ApprovalNodeRecord record = new ApprovalNodeRecord();
                        record.setId(IDGenerator.nextStr());
                        record.setInstanceId(instance.getId());
                        record.setNodeSeq(nextNodeSeq);
                        record.setApproverId(approverId);
                        record.setStatus("pending");
                        record.setCreateUser(userId);
                        record.setUpdateUser(userId);
                        record.setCreateTime(now);
                        record.setUpdateTime(now);
                        newRecords.add(record);
                    }
                    approvalNodeRecordMapper.batchInsert(newRecords);
                }
            }
        }
    }

    /**
     * 审批驳回
     * <p>
     * 更新 NodeRecord.status=rejected → instance.status=rejected
     *
     * @param request 审批操作请求
     * @param userId  当前用户 ID
     */
    public void reject(ApprovalOperateRequest request, String userId) {
        ApprovalInstance instance = approvalInstanceMapper.selectByPrimaryKey(request.getInstanceId());
        if (instance == null) {
            throw new GenericException(Translator.get("approval.instance.not.exist"));
        }

        if (!StringUtils.equals(instance.getStatus(), "pending")) {
            throw new GenericException(Translator.get("approval.instance.not.pending"));
        }

        // 查找当前用户在当前节点的待审批记录
        ApprovalNodeRecord currentRecord = findPendingRecord(instance.getId(), instance.getCurrentNodeSeq(), userId);
        if (currentRecord == null) {
            throw new GenericException(Translator.get("approval.node.not.pending"));
        }

        long now = System.currentTimeMillis();

        // 更新当前节点记录为驳回
        currentRecord.setStatus("rejected");
        currentRecord.setComment(request.getComment());
        currentRecord.setOperateTime(now);
        currentRecord.setUpdateUser(userId);
        currentRecord.setUpdateTime(now);
        approvalNodeRecordMapper.update(currentRecord);

        // 整个实例变为 rejected
        instance.setStatus("rejected");
        instance.setUpdateUser(userId);
        instance.setUpdateTime(now);
        approvalInstanceMapper.update(instance);
    }

    /**
     * 审批实例分页列表
     *
     * @param request 分页请求
     * @param userId  当前用户 ID
     * @param orgId   组织 ID
     * @return 分页结果
     */
    public PagerWithOption<List<ApprovalInstanceListResponse>> list(ApprovalInstancePageRequest request, String userId, String orgId) {
        PageHelper.startPage(request.getCurrent(), request.getPageSize());

        List<ApprovalInstance> instances;
        String type = request.getType();
        if ("pending".equals(type)) {
            // 待我审批的
            instances = extApprovalInstanceMapper.selectPendingPage(
                    userId, orgId, request.getBizType(), request.getKeyword());
        } else if ("mine".equals(type)) {
            // 我发起的
            instances = extApprovalInstanceMapper.selectMinePage(
                    userId, orgId, request.getBizType(), request.getStatus(), request.getKeyword());
        } else {
            // 全部
            instances = extApprovalInstanceMapper.selectAllPage(
                    orgId, request.getBizType(), request.getStatus(), request.getKeyword());
        }

        PageInfo<ApprovalInstance> pageInfo = new PageInfo<>(instances);

        // 批量查询申请人姓名
        List<String> applicantIds = instances.stream()
                .map(ApprovalInstance::getApplicant)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .toList();
        Map<String, String> userNameMap = Map.of();
        if (!applicantIds.isEmpty()) {
            List<User> users = userMapper.selectByIds(applicantIds);
            userNameMap = users.stream()
                    .filter(u -> StringUtils.isNotBlank(u.getName()))
                    .collect(Collectors.toMap(User::getId, User::getName, (a, b) -> a));
        }

        Map<String, String> finalUserNameMap = userNameMap;
        List<ApprovalInstanceListResponse> responseList = instances.stream()
                .map(instance -> {
                    ApprovalInstanceListResponse response = BeanUtils.copyBean(new ApprovalInstanceListResponse(), instance);
                    if (StringUtils.isNotBlank(instance.getApplicant()) && finalUserNameMap.containsKey(instance.getApplicant())) {
                        response.setApplicantName(finalUserNameMap.get(instance.getApplicant()));
                    }
                    return response;
                })
                .toList();

        return PagerWithOption.of(responseList, pageInfo.getTotal(),
                request.getCurrent(), request.getPageSize());
    }

    /**
     * 审批实例详情（含节点记录）
     *
     * @param id 实例 ID
     * @return 实例详情响应
     */
    public ApprovalInstanceGetResponse get(String id) {
        ApprovalInstance instance = approvalInstanceMapper.selectByPrimaryKey(id);
        if (instance == null) {
            return null;
        }

        ApprovalInstanceGetResponse response = BeanUtils.copyBean(new ApprovalInstanceGetResponse(), instance);

        // 查询申请人姓名
        if (StringUtils.isNotBlank(instance.getApplicant())) {
            User applicant = userMapper.selectByPrimaryKey(instance.getApplicant());
            if (applicant != null) {
                response.setApplicantName(applicant.getName());
            }
        }

        // 查询模板节点配置
        ApprovalTemplate template = approvalTemplateService.getEnabledByBizType(
                        instance.getBizType(), instance.getOrganizationId())
                .stream()
                .filter(t -> StringUtils.equals(t.getId(), instance.getTemplateId()))
                .findFirst()
                .orElse(null);
        if (template != null) {
            response.setNodes(template.getNodes());
        }

        // 查询节点记录列表
        List<ApprovalNodeRecord> nodeRecords = extApprovalNodeRecordMapper.selectByInstanceId(id);

        // 批量查询审批人姓名
        List<String> approverIds = nodeRecords.stream()
                .map(ApprovalNodeRecord::getApproverId)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .toList();
        Map<String, String> userNameMap = Map.of();
        if (!approverIds.isEmpty()) {
            List<User> users = userMapper.selectByIds(approverIds);
            userNameMap = users.stream()
                    .filter(u -> StringUtils.isNotBlank(u.getName()))
                    .collect(Collectors.toMap(User::getId, User::getName, (a, b) -> a));
        }

        Map<String, String> finalUserNameMap = userNameMap;
        List<ApprovalNodeRecordResponse> recordResponses = nodeRecords.stream()
                .map(record -> {
                    ApprovalNodeRecordResponse recordResponse = BeanUtils.copyBean(new ApprovalNodeRecordResponse(), record);
                    if (StringUtils.isNotBlank(record.getApproverId()) && finalUserNameMap.containsKey(record.getApproverId())) {
                        recordResponse.setApproverName(finalUserNameMap.get(record.getApproverId()));
                    }
                    return recordResponse;
                })
                .toList();

        response.setNodeRecords(recordResponses);
        return response;
    }

    /**
     * 查找当前用户在指定实例的指定节点上的待审批记录
     *
     * @param instanceId 实例 ID
     * @param nodeSeq    节点序号
     * @param userId     当前用户 ID
     * @return 待审批的节点记录
     */
    private ApprovalNodeRecord findPendingRecord(String instanceId, Integer nodeSeq, String userId) {
        List<ApprovalNodeRecord> pendingRecords = extApprovalNodeRecordMapper
                .selectPendingByInstanceAndSeq(instanceId, nodeSeq);
        return pendingRecords.stream()
                .filter(r -> StringUtils.equals(r.getApproverId(), userId))
                .findFirst()
                .orElse(null);
    }

    /**
     * 解析节点配置 JSON
     *
     * @param nodesJson 节点 JSON 字符串
     * @return 节点配置列表
     */
    private List<Map<String, Object>> parseNodes(String nodesJson) {
        if (StringUtils.isBlank(nodesJson)) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(nodesJson, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            log.error("解析审批节点配置失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
