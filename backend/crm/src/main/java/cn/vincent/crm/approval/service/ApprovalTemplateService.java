package cn.vincent.crm.approval.service;

import cn.vincent.common.exception.GenericException;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.approval.domain.ApprovalTemplate;
import cn.vincent.crm.approval.dto.request.ApprovalTemplateAddRequest;
import cn.vincent.crm.approval.dto.request.ApprovalTemplateEnableRequest;
import cn.vincent.crm.approval.dto.request.ApprovalTemplatePageRequest;
import cn.vincent.crm.approval.dto.request.ApprovalTemplateUpdateRequest;
import cn.vincent.crm.approval.dto.response.ApprovalTemplateGetResponse;
import cn.vincent.crm.approval.dto.response.ApprovalTemplateListResponse;
import cn.vincent.crm.approval.mapper.ApprovalTemplateMapper;
import cn.vincent.crm.approval.mapper.ExtApprovalTemplateMapper;
import cn.vincent.crm.system.service.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 审批模板服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ApprovalTemplateService {

    @Resource
    private ApprovalTemplateMapper approvalTemplateMapper;

    @Resource
    private ExtApprovalTemplateMapper extApprovalTemplateMapper;

    @Resource
    private BaseService baseService;

    /**
     * 新增审批模板
     *
     * @param request 新增请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 新增的审批模板实体
     */
    public ApprovalTemplate add(ApprovalTemplateAddRequest request, String userId, String orgId) {
        ApprovalTemplate template = new ApprovalTemplate();
        template.setId(IDGenerator.nextStr());
        template.setName(request.getName());
        template.setBizType(request.getBizType());
        template.setDescription(request.getDescription());
        template.setEnabled(true);
        template.setNodes(request.getNodes());
        template.setOrganizationId(orgId);
        template.setCreateUser(userId);
        template.setUpdateUser(userId);
        template.setCreateTime(System.currentTimeMillis());
        template.setUpdateTime(System.currentTimeMillis());
        approvalTemplateMapper.insert(template);
        return template;
    }

    /**
     * 更新审批模板
     *
     * @param request 更新请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 更新后的审批模板实体
     */
    public ApprovalTemplate update(ApprovalTemplateUpdateRequest request, String userId, String orgId) {
        ApprovalTemplate template = approvalTemplateMapper.selectByPrimaryKey(request.getId());
        if (template == null) {
            throw new GenericException(Translator.get("approval.template.not.exist"));
        }

        if (request.getName() != null) {
            template.setName(request.getName());
        }
        if (request.getBizType() != null) {
            template.setBizType(request.getBizType());
        }
        if (request.getDescription() != null) {
            template.setDescription(request.getDescription());
        }
        if (request.getNodes() != null) {
            template.setNodes(request.getNodes());
        }
        template.setUpdateUser(userId);
        template.setUpdateTime(System.currentTimeMillis());
        approvalTemplateMapper.update(template);
        return template;
    }

    /**
     * 删除审批模板
     *
     * @param id 模板 ID
     */
    public void delete(String id) {
        ApprovalTemplate template = approvalTemplateMapper.selectByPrimaryKey(id);
        if (template == null) {
            throw new GenericException(Translator.get("approval.template.not.exist"));
        }
        approvalTemplateMapper.deleteByIds(List.of(id));
    }

    /**
     * 审批模板分页列表
     *
     * @param request 分页请求
     * @param orgId   组织 ID
     * @return 分页结果
     */
    public PagerWithOption<List<ApprovalTemplateListResponse>> list(ApprovalTemplatePageRequest request, String orgId) {
        PageHelper.startPage(request.getCurrent(), request.getPageSize());
        List<ApprovalTemplate> templates = extApprovalTemplateMapper.selectTemplatePage(
                orgId, request.getKeyword(), request.getBizType(), request.getEnabled());
        PageInfo<ApprovalTemplate> pageInfo = new PageInfo<>(templates);

        List<ApprovalTemplateListResponse> responseList = templates.stream()
                .map(template -> BeanUtils.copyBean(new ApprovalTemplateListResponse(), template))
                .toList();

        responseList = baseService.setCreateAndUpdateUserName(responseList);

        return PagerWithOption.of(responseList, pageInfo.getTotal(),
                request.getCurrent(), request.getPageSize());
    }

    /**
     * 审批模板详情
     *
     * @param id 模板 ID
     * @return 模板详情响应
     */
    public ApprovalTemplateGetResponse get(String id) {
        ApprovalTemplate template = approvalTemplateMapper.selectByPrimaryKey(id);
        if (template == null) {
            return null;
        }

        ApprovalTemplateGetResponse response = BeanUtils.copyBean(new ApprovalTemplateGetResponse(), template);
        baseService.setCreateUpdateOwnerUserName(response);
        return response;
    }

    /**
     * 启用/禁用审批模板
     *
     * @param request 启用/禁用请求
     * @param userId  当前用户 ID
     */
    public void enable(ApprovalTemplateEnableRequest request, String userId) {
        ApprovalTemplate template = approvalTemplateMapper.selectByPrimaryKey(request.getId());
        if (template == null) {
            throw new GenericException(Translator.get("approval.template.not.exist"));
        }
        template.setEnabled(request.getEnabled());
        template.setUpdateUser(userId);
        template.setUpdateTime(System.currentTimeMillis());
        approvalTemplateMapper.update(template);
    }

    /**
     * 根据业务类型查询启用的审批模板
     *
     * @param bizType 业务类型
     * @param orgId   组织 ID
     * @return 启用的审批模板列表
     */
    public List<ApprovalTemplate> getEnabledByBizType(String bizType, String orgId) {
        return extApprovalTemplateMapper.selectEnabledByBizType(bizType, orgId);
    }
}
