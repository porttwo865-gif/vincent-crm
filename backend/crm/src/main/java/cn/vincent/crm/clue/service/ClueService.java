package cn.vincent.crm.clue.service;

import cn.vincent.common.constants.FormKey;
import cn.vincent.common.exception.GenericException;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.ConditionFilterUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.clue.constants.ClueConstants;
import cn.vincent.crm.clue.domain.Clue;
import cn.vincent.crm.clue.domain.ClueOwner;
import cn.vincent.crm.clue.dto.request.ClueAddRequest;
import cn.vincent.crm.clue.dto.request.ClueMovePoolRequest;
import cn.vincent.crm.clue.dto.request.CluePageRequest;
import cn.vincent.crm.clue.dto.request.ClueTransformRequest;
import cn.vincent.crm.clue.dto.request.ClueUpdateRequest;
import cn.vincent.crm.clue.dto.response.ClueGetResponse;
import cn.vincent.crm.clue.dto.response.ClueListResponse;
import cn.vincent.crm.clue.mapper.ClueMapper;
import cn.vincent.crm.clue.mapper.ClueOwnerMapper;
import cn.vincent.crm.clue.mapper.ExtClueMapper;
import cn.vincent.crm.clue.mapper.ExtClueOwnerMapper;
import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import cn.vincent.crm.system.service.BaseService;
import cn.vincent.crm.system.service.ModuleFieldValueService;
import cn.vincent.security.DataScopeService;
import cn.vincent.security.dto.DeptDataPermissionDTO;
import com.github.pagehelper.PageHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 线索管理服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ClueService {

    /** 线索通用 Mapper */
    @Resource
    private ClueMapper clueMapper;

    /** 线索自定义 Mapper */
    @Resource
    private ExtClueMapper extClueMapper;

    /** 线索负责人变更历史自定义 Mapper */
    @Resource
    private ExtClueOwnerMapper extClueOwnerMapper;

    /** 线索负责人变更历史 Mapper */
    @Resource
    private ClueOwnerMapper clueOwnerMapper;

    /** 数据权限服务 */
    @Resource
    private DataScopeService dataScopeService;

    /** 通用基础服务 */
    @Resource
    private BaseService baseService;

    /** 自定义字段值服务 */
    @Resource
    private ModuleFieldValueService moduleFieldValueService;

    /**
     * 新增线索
     *
     * @param request 新增请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 新增的线索实体
     */
    public Clue add(ClueAddRequest request, String userId, String orgId) {
        Clue clue = new Clue();
        clue.setId(IDGenerator.nextStr());
        clue.setName(request.getName());
        clue.setOwner(StringUtils.isNotBlank(request.getOwner()) ? request.getOwner() : userId);
        clue.setContact(request.getContact());
        clue.setPhone(request.getPhone());
        // 将产品 ID 列表转为 JSON 字符串存储
        if (request.getProducts() != null && !request.getProducts().isEmpty()) {
            clue.setProducts(toJsonString(request.getProducts()));
        }
        clue.setOrganizationId(orgId);
        clue.setInSharedPool(false);
        clue.setCreateUser(userId);
        clue.setUpdateUser(userId);
        clue.setCreateTime(System.currentTimeMillis());
        clue.setUpdateTime(System.currentTimeMillis());
        clueMapper.insert(clue);

        // 保存自定义字段值
        if (request.getModuleFields() != null && !request.getModuleFields().isEmpty()) {
            moduleFieldValueService.saveFieldValues(
                    FormKey.CLUE.getKey(), clue.getId(), request.getModuleFields(), userId);
        }

        // 记录负责人变更历史
        saveOwnerHistory(clue.getId(), null, clue.getOwner(), ClueConstants.OPERATION_ADD, userId, orgId);

        return clue;
    }

    /**
     * 更新线索
     *
     * @param request 更新请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 更新后的线索实体
     */
    public Clue update(ClueUpdateRequest request, String userId, String orgId) {
        Clue clue = clueMapper.selectByPrimaryKey(request.getId());
        if (clue == null) {
            throw new GenericException(Translator.get("clue.not.exist"));
        }

        // 数据权限校验
        dataScopeService.checkDataPermission(userId, orgId, List.of(clue.getOwner()),
                cn.vincent.common.constants.PermissionConstants.CLUE_MANAGEMENT_UPDATE);

        if (request.getName() != null) {
            clue.setName(request.getName());
        }
        if (request.getOwner() != null) {
            // 负责人变更时记录历史
            String oldOwner = clue.getOwner();
            clue.setOwner(request.getOwner());
            if (!StringUtils.equals(oldOwner, request.getOwner())) {
                saveOwnerHistory(clue.getId(), oldOwner, request.getOwner(),
                        ClueConstants.OPERATION_TRANSFER, userId, orgId);
            }
        }
        if (request.getContact() != null) {
            clue.setContact(request.getContact());
        }
        if (request.getPhone() != null) {
            clue.setPhone(request.getPhone());
        }
        if (request.getProducts() != null) {
            clue.setProducts(toJsonString(request.getProducts()));
        }
        clue.setUpdateUser(userId);
        clue.setUpdateTime(System.currentTimeMillis());
        clueMapper.update(clue);

        // 更新自定义字段值
        if (request.getModuleFields() != null) {
            moduleFieldValueService.saveFieldValues(
                    FormKey.CLUE.getKey(), clue.getId(), request.getModuleFields(), userId);
        }

        return clue;
    }

    /**
     * 删除线索
     *
     * @param id 线索 ID
     */
    public void delete(String id) {
        Clue clue = clueMapper.selectByPrimaryKey(id);
        if (clue == null) {
            throw new GenericException(Translator.get("clue.not.exist"));
        }

        clueMapper.deleteByIds(List.of(id));

        // 删除关联的自定义字段值
        moduleFieldValueService.deleteFieldValues(FormKey.CLUE.getKey(), id);
    }

    /**
     * 批量删除线索
     *
     * @param ids 线索 ID 列表
     */
    public void batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        clueMapper.deleteByIds(ids);

        // 删除关联的自定义字段值
        for (String id : ids) {
            moduleFieldValueService.deleteFieldValues(FormKey.CLUE.getKey(), id);
        }
    }

    /**
     * 线索分页列表
     *
     * @param request            分页请求
     * @param userId             当前用户 ID
     * @param orgId              当前组织 ID
     * @param deptDataPermission 部门数据权限
     * @return 分页响应
     */
    public PagerWithOption<List<ClueListResponse>> list(CluePageRequest request, String userId,
                                                         String orgId, DeptDataPermissionDTO deptDataPermission) {
        // 解析条件筛选
        ConditionFilterUtils.parseCondition(request, FormKey.CLUE.getKey());

        // 数据权限过滤：获取可见负责人 ID 列表
        List<String> ownerIds = null;
        if (deptDataPermission != null && !deptDataPermission.isAll()) {
            ownerIds = deptDataPermission.getUserIds();
        }

        // PageHelper 分页
        int current = request.getCurrent() != null ? request.getCurrent() : 1;
        int pageSize = request.getPageSize() != null ? request.getPageSize() : 20;
        PageHelper.startPage(current, pageSize);

        List<Clue> clues = extClueMapper.selectByCondition(
                orgId, request.getInSharedPool(), ownerIds, request.getKeyword());

        // 转换为分页结果
        com.github.pagehelper.Page<Clue> page = (com.github.pagehelper.Page<Clue>) clues;
        List<ClueListResponse> responseList = convertToListResponse(clues, orgId);

        return PagerWithOption.of(responseList, page.getTotal(), current, pageSize);
    }

    /**
     * 查询线索详情
     *
     * @param id 线索 ID
     * @return 线索详情响应
     */
    public ClueGetResponse get(String id) {
        Clue clue = clueMapper.selectByPrimaryKey(id);
        if (clue == null) {
            return null;
        }

        ClueGetResponse response = BeanUtils.copyBean(new ClueGetResponse(), clue);

        // 解析产品 ID 列表
        response.setProducts(parseJsonToList(clue.getProducts()));

        // 加载自定义字段值
        List<ModuleFieldValueDTO> fieldValues = moduleFieldValueService.getFieldValues(
                FormKey.CLUE.getKey(), id);
        response.setModuleFields(fieldValues);

        // 设置创建人/更新人/负责人姓名
        baseService.setCreateUpdateOwnerUserName(response);

        return response;
    }

    /**
     * 查询线索详情（带数据权限校验）
     *
     * @param id     线索 ID
     * @param userId 当前用户 ID
     * @param orgId  当前组织 ID
     * @return 线索详情响应
     */
    public ClueGetResponse getWithDataPermissionCheck(String id, String userId, String orgId) {
        ClueGetResponse response = get(id);
        if (response == null) {
            throw new GenericException(Translator.get("clue.not.exist"));
        }

        // 数据权限校验
        dataScopeService.checkDataPermission(userId, orgId, List.of(response.getOwner()),
                cn.vincent.common.constants.PermissionConstants.CLUE_MANAGEMENT_READ);

        // 加载负责人变更历史
        List<ClueOwner> ownerHistory = getOwnerHistory(id);
        response.setOwnerHistory(ownerHistory);

        return response;
    }

    /**
     * 线索转客户
     *
     * @param request 转化请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 更新后的线索实体
     */
    public Clue transform(ClueTransformRequest request, String userId, String orgId) {
        Clue clue = clueMapper.selectByPrimaryKey(request.getClueId());
        if (clue == null) {
            throw new GenericException(Translator.get("clue.not.exist"));
        }

        // 校验线索是否已转化
        if (StringUtils.isNotBlank(clue.getTransitionType())) {
            throw new GenericException(Translator.get("clue.already.transformed"));
        }

        // 数据权限校验
        dataScopeService.checkDataPermission(userId, orgId, List.of(clue.getOwner()),
                cn.vincent.common.constants.PermissionConstants.CLUE_MANAGEMENT_TRANSFORM);

        // 暂不实际创建客户实体（客户模块由另一个工程师并行开发）
        // 只更新线索的 transitionType 和 transitionId
        String transitionId = null;
        if ("LINK".equals(request.getMode()) && StringUtils.isNotBlank(request.getCustomerId())) {
            // 关联已有客户
            transitionId = request.getCustomerId();
        } else if ("NEW".equals(request.getMode())) {
            // 新建客户模式：暂时生成一个占位 ID
            // 客户模块实现后，此处应调用客户服务创建客户并获取真实 ID
            transitionId = IDGenerator.nextStr();
            log.info("线索 {} 转客户（新建模式），占位 transitionId: {}", clue.getId(), transitionId);
        }

        clue.setTransitionType("CUSTOMER");
        clue.setTransitionId(transitionId);
        clue.setUpdateUser(userId);
        clue.setUpdateTime(System.currentTimeMillis());
        clueMapper.update(clue);

        return clue;
    }

    /**
     * 批量移入线索池
     *
     * @param request 移入线索池请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     */
    public void moveToPool(ClueMovePoolRequest request, String userId, String orgId) {
        List<String> ids = request.getIds();
        if (ids == null || ids.isEmpty()) {
            return;
        }

        // 批量更新线索池状态
        extClueMapper.batchUpdatePoolStatus(ids, true, null, null,
                request.getReasonId(), userId, System.currentTimeMillis());

        // 记录每条线索的负责人变更历史
        List<Clue> clues = clueMapper.selectByIds(ids);
        for (Clue clue : clues) {
            saveOwnerHistory(clue.getId(), clue.getOwner(), null,
                    ClueConstants.OPERATION_MOVE_POOL, userId, orgId);
        }
    }

    /**
     * 从线索池领取（设置负责人、移出线索池）
     *
     * @param clueId 线索 ID
     * @param userId 领取人 ID
     * @param orgId  组织 ID
     */
    public void claimFromPool(String clueId, String userId, String orgId) {
        Clue clue = clueMapper.selectByPrimaryKey(clueId);
        if (clue == null) {
            throw new GenericException(Translator.get("clue.not.exist"));
        }

        String oldOwner = clue.getOwner();
        clue.setOwner(userId);
        clue.setInSharedPool(false);
        clue.setCollectionTime(System.currentTimeMillis());
        clue.setUpdateUser(userId);
        clue.setUpdateTime(System.currentTimeMillis());
        clueMapper.update(clue);

        // 记录负责人变更历史
        saveOwnerHistory(clueId, oldOwner, userId, ClueConstants.OPERATION_CLAIM, userId, orgId);
    }

    /**
     * 从线索池分配（管理员分配给指定负责人）
     *
     * @param clueId  线索 ID
     * @param toOwner 目标负责人 ID
     * @param userId  操作人 ID
     * @param orgId   组织 ID
     */
    public void assignFromPool(String clueId, String toOwner, String userId, String orgId) {
        Clue clue = clueMapper.selectByPrimaryKey(clueId);
        if (clue == null) {
            throw new GenericException(Translator.get("clue.not.exist"));
        }

        String oldOwner = clue.getOwner();
        clue.setOwner(toOwner);
        clue.setInSharedPool(false);
        clue.setCollectionTime(System.currentTimeMillis());
        clue.setUpdateUser(userId);
        clue.setUpdateTime(System.currentTimeMillis());
        clueMapper.update(clue);

        // 记录负责人变更历史
        saveOwnerHistory(clueId, oldOwner, toOwner, ClueConstants.OPERATION_ASSIGN, userId, orgId);
    }

    /**
     * Jackson JSON 序列化工具
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 保存负责人变更历史
     *
     * @param clueId        线索 ID
     * @param fromOwner     原负责人
     * @param toOwner       新负责人
     * @param operationType 操作类型
     * @param operatorId    操作人
     * @param orgId         组织 ID
     */
    private void saveOwnerHistory(String clueId, String fromOwner, String toOwner,
                                   String operationType, String operatorId, String orgId) {
        ClueOwner ownerRecord = new ClueOwner();
        ownerRecord.setId(IDGenerator.nextStr());
        ownerRecord.setClueId(clueId);
        ownerRecord.setFromOwner(fromOwner);
        ownerRecord.setToOwner(toOwner);
        ownerRecord.setOperationType(operationType);
        ownerRecord.setOperatorId(operatorId);
        ownerRecord.setOperateTime(System.currentTimeMillis());
        ownerRecord.setOrganizationId(orgId);
        ownerRecord.setCreateUser(operatorId);
        ownerRecord.setUpdateUser(operatorId);
        ownerRecord.setCreateTime(System.currentTimeMillis());
        ownerRecord.setUpdateTime(System.currentTimeMillis());
        clueOwnerMapper.insert(ownerRecord);
    }

    /**
     * 查询线索的负责人变更历史
     *
     * @param clueId 线索 ID
     * @return 变更历史列表
     */
    private List<ClueOwner> getOwnerHistory(String clueId) {
        return extClueOwnerMapper.selectByClueId(clueId);
    }

    /**
     * 将列表转换为 ClueListResponse
     *
     * @param clues 线索列表
     * @param orgId 组织 ID
     * @return 响应列表
     */
    private List<ClueListResponse> convertToListResponse(List<Clue> clues, String orgId) {
        if (clues == null || clues.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> clueIds = clues.stream().map(Clue::getId).toList();

        // 批量获取自定义字段值
        Map<String, List<ModuleFieldValueDTO>> fieldValuesMap =
                moduleFieldValueService.batchGetFieldValues(FormKey.CLUE.getKey(), clueIds);

        List<ClueListResponse> responseList = new ArrayList<>();
        for (Clue clue : clues) {
            ClueListResponse response = BeanUtils.copyBean(new ClueListResponse(), clue);
            response.setProducts(parseJsonToList(clue.getProducts()));
            response.setModuleFields(fieldValuesMap.getOrDefault(clue.getId(), new ArrayList<>()));
            responseList.add(response);
        }

        // 批量设置创建人/更新人姓名
        baseService.setCreateAndUpdateUserName(responseList);

        return responseList;
    }

    /**
     * 将 List 转为 JSON 字符串（使用 Jackson 序列化）
     *
     * @param list 列表
     * @return JSON 字符串
     */
    private String toJsonString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            log.error("JSON 序列化失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串解析为 List（使用 Jackson 反序列化）
     *
     * @param json JSON 字符串
     * @return 列表
     */
    private List<String> parseJsonToList(String json) {
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>();
        }
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            log.error("JSON 解析失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
