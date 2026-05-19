package cn.vincent.crm.opportunity.service;

import cn.vincent.common.constants.FormKey;
import cn.vincent.common.exception.GenericException;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.ConditionFilterUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.customer.domain.Customer;
import cn.vincent.crm.customer.domain.CustomerContact;
import cn.vincent.crm.customer.mapper.CustomerContactMapper;
import cn.vincent.crm.customer.mapper.CustomerMapper;
import cn.vincent.crm.opportunity.domain.Opportunity;
import cn.vincent.crm.opportunity.dto.request.OpportunityAddRequest;
import cn.vincent.crm.opportunity.dto.request.OpportunityPageRequest;
import cn.vincent.crm.opportunity.dto.request.OpportunityPosRequest;
import cn.vincent.crm.opportunity.dto.request.OpportunityStageRequest;
import cn.vincent.crm.opportunity.dto.request.OpportunityUpdateRequest;
import cn.vincent.crm.opportunity.dto.response.OpportunityGetResponse;
import cn.vincent.crm.opportunity.dto.response.OpportunityListResponse;
import cn.vincent.crm.opportunity.mapper.ExtOpportunityMapper;
import cn.vincent.crm.opportunity.mapper.OpportunityMapper;
import cn.vincent.crm.system.domain.User;
import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import cn.vincent.crm.system.mapper.UserMapper;
import cn.vincent.crm.system.service.BaseService;
import cn.vincent.crm.system.service.ModuleFieldValueService;
import cn.vincent.security.DataScopeService;
import cn.vincent.security.dto.DeptDataPermissionDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商机管理服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class OpportunityService {

    /** 商机通用 Mapper */
    @Resource
    private OpportunityMapper opportunityMapper;

    /** 商机自定义 Mapper */
    @Resource
    private ExtOpportunityMapper extOpportunityMapper;

    /** 客户 Mapper */
    @Resource
    private CustomerMapper customerMapper;

    /** 客户联系人 Mapper */
    @Resource
    private CustomerContactMapper customerContactMapper;

    /** 用户 Mapper */
    @Resource
    private UserMapper userMapper;

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
     * 新增商机
     *
     * @param request 新增请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 新增的商机实体
     */
    public Opportunity add(OpportunityAddRequest request, String userId, String orgId) {
        Opportunity opportunity = new Opportunity();
        opportunity.setId(IDGenerator.nextStr());
        opportunity.setName(request.getName());
        opportunity.setCustomerId(request.getCustomerId());
        opportunity.setContactId(request.getContactId());
        opportunity.setOwner(StringUtils.isNotBlank(request.getOwner()) ? request.getOwner() : userId);
        opportunity.setStage(request.getStage());
        opportunity.setAmount(request.getAmount());
        opportunity.setExpectedCloseTime(request.getExpectedCloseTime());
        opportunity.setRemark(request.getRemark());
        opportunity.setPos(System.currentTimeMillis());
        opportunity.setOrganizationId(orgId);
        opportunity.setCreateUser(userId);
        opportunity.setUpdateUser(userId);
        opportunity.setCreateTime(System.currentTimeMillis());
        opportunity.setUpdateTime(System.currentTimeMillis());
        opportunityMapper.insert(opportunity);

        // 保存自定义字段值
        if (request.getModuleFields() != null && !request.getModuleFields().isEmpty()) {
            moduleFieldValueService.saveFieldValues(
                    FormKey.OPPORTUNITY.getKey(), opportunity.getId(), request.getModuleFields(), userId);
        }

        return opportunity;
    }

    /**
     * 更新商机
     *
     * @param request 更新请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 更新后的商机实体
     */
    public Opportunity update(OpportunityUpdateRequest request, String userId, String orgId) {
        Opportunity opportunity = opportunityMapper.selectByPrimaryKey(request.getId());
        if (opportunity == null) {
            throw new GenericException(Translator.get("opportunity.not.exist"));
        }

        if (request.getName() != null) {
            opportunity.setName(request.getName());
        }
        if (request.getCustomerId() != null) {
            opportunity.setCustomerId(request.getCustomerId());
        }
        if (request.getContactId() != null) {
            opportunity.setContactId(request.getContactId());
        }
        if (request.getOwner() != null) {
            opportunity.setOwner(request.getOwner());
        }
        if (request.getStage() != null) {
            // 记录上次阶段
            opportunity.setLastStage(opportunity.getStage());
            opportunity.setStage(request.getStage());
        }
        if (request.getAmount() != null) {
            opportunity.setAmount(request.getAmount());
        }
        if (request.getExpectedCloseTime() != null) {
            opportunity.setExpectedCloseTime(request.getExpectedCloseTime());
        }
        if (request.getRemark() != null) {
            opportunity.setRemark(request.getRemark());
        }
        opportunity.setUpdateUser(userId);
        opportunity.setUpdateTime(System.currentTimeMillis());
        opportunityMapper.update(opportunity);

        // 更新自定义字段值
        if (request.getModuleFields() != null) {
            moduleFieldValueService.saveFieldValues(
                    FormKey.OPPORTUNITY.getKey(), opportunity.getId(), request.getModuleFields(), userId);
        }

        return opportunity;
    }

    /**
     * 删除商机
     *
     * @param id 商机 ID
     */
    public void delete(String id) {
        Opportunity opportunity = opportunityMapper.selectByPrimaryKey(id);
        if (opportunity == null) {
            throw new GenericException(Translator.get("opportunity.not.exist"));
        }

        // 删除自定义字段值
        moduleFieldValueService.deleteFieldValues(FormKey.OPPORTUNITY.getKey(), id);

        // 删除商机
        opportunityMapper.deleteByIds(List.of(id));
    }

    /**
     * 批量删除商机
     *
     * @param ids 商机 ID 列表
     */
    public void batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        opportunityMapper.deleteByIds(ids);

        // 删除关联的自定义字段值
        for (String id : ids) {
            moduleFieldValueService.deleteFieldValues(FormKey.OPPORTUNITY.getKey(), id);
        }
    }

    /**
     * 商机分页列表
     *
     * @param request            分页请求
     * @param userId             当前用户 ID
     * @param orgId              当前组织 ID
     * @param deptDataPermission 部门数据权限
     * @return 分页响应
     */
    public PagerWithOption<List<OpportunityListResponse>> list(OpportunityPageRequest request, String userId,
                                                                String orgId, DeptDataPermissionDTO deptDataPermission) {
        // 解析条件筛选
        ConditionFilterUtils.parseCondition(request, FormKey.OPPORTUNITY.getKey());

        // 数据权限过滤：获取可见负责人 ID 列表
        List<String> ownerIds = null;
        if (deptDataPermission != null && !deptDataPermission.isAll()) {
            ownerIds = deptDataPermission.getUserIds();
        }

        // PageHelper 分页
        int current = request.getCurrent() != null ? request.getCurrent() : 1;
        int pageSize = request.getPageSize() != null ? request.getPageSize() : 20;
        PageHelper.startPage(current, pageSize);

        List<Opportunity> opportunities = extOpportunityMapper.selectByCondition(
                orgId, ownerIds, request.getStage());
        PageInfo<Opportunity> pageInfo = new PageInfo<>(opportunities);

        // 转换为响应 DTO
        List<OpportunityListResponse> responseList = convertToListResponse(opportunities);

        return PagerWithOption.of(responseList, pageInfo.getTotal(), current, pageSize);
    }

    /**
     * 商机详情
     *
     * @param id 商机 ID
     * @return 商机详情响应
     */
    public OpportunityGetResponse get(String id) {
        Opportunity opportunity = opportunityMapper.selectByPrimaryKey(id);
        if (opportunity == null) {
            return null;
        }

        OpportunityGetResponse response = BeanUtils.copyBean(new OpportunityGetResponse(), opportunity);

        // 查询自定义字段值
        List<ModuleFieldValueDTO> fieldValues = moduleFieldValueService.getFieldValues(
                FormKey.OPPORTUNITY.getKey(), id);
        response.setModuleFields(fieldValues);

        // 设置创建人/更新人/负责人姓名
        baseService.setCreateUpdateOwnerUserName(response);

        // 设置客户名称
        if (StringUtils.isNotBlank(opportunity.getCustomerId())) {
            Customer customer = customerMapper.selectByPrimaryKey(opportunity.getCustomerId());
            if (customer != null) {
                response.setCustomerName(customer.getName());
            }
        }

        // 设置联系人名称
        if (StringUtils.isNotBlank(opportunity.getContactId())) {
            CustomerContact contact = customerContactMapper.selectByPrimaryKey(opportunity.getContactId());
            if (contact != null) {
                response.setContactName(contact.getName());
            }
        }

        // 设置跟进人姓名
        if (StringUtils.isNotBlank(opportunity.getFollower())) {
            User follower = userMapper.selectByPrimaryKey(opportunity.getFollower());
            if (follower != null) {
                response.setFollowerName(follower.getName());
            }
        }

        return response;
    }

    /**
     * 商机详情（含数据权限校验）
     *
     * @param id     商机 ID
     * @param userId 当前用户 ID
     * @param orgId  当前组织 ID
     * @return 商机详情响应
     */
    public OpportunityGetResponse getWithDataPermissionCheck(String id, String userId, String orgId) {
        OpportunityGetResponse response = get(id);
        if (response == null) {
            throw new GenericException(Translator.get("opportunity.not.exist"));
        }

        // 数据权限校验
        dataScopeService.checkDataPermission(userId, orgId, List.of(response.getOwner()),
                cn.vincent.common.constants.PermissionConstants.OPPORTUNITY_MANAGEMENT_READ);

        return response;
    }

    /**
     * 更新商机阶段
     *
     * @param request 阶段更新请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     */
    public void updateStage(OpportunityStageRequest request, String userId, String orgId) {
        Opportunity opportunity = opportunityMapper.selectByPrimaryKey(request.getId());
        if (opportunity == null) {
            throw new GenericException(Translator.get("opportunity.not.exist"));
        }

        // 记录上次阶段
        opportunity.setLastStage(opportunity.getStage());
        opportunity.setStage(request.getStage());
        opportunity.setUpdateUser(userId);
        opportunity.setUpdateTime(System.currentTimeMillis());
        opportunityMapper.update(opportunity);
    }

    /**
     * 更新看板拖拽排序位置
     *
     * @param request 排序请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     */
    public void updatePos(OpportunityPosRequest request, String userId, String orgId) {
        Opportunity opportunity = opportunityMapper.selectByPrimaryKey(request.getId());
        if (opportunity == null) {
            throw new GenericException(Translator.get("opportunity.not.exist"));
        }

        // 更新排序位置
        if (request.getPos() != null) {
            opportunity.setPos(request.getPos());
        }

        // 跨阶段拖拽时更新阶段
        if (StringUtils.isNotBlank(request.getStage())) {
            opportunity.setLastStage(opportunity.getStage());
            opportunity.setStage(request.getStage());
        }

        opportunity.setUpdateUser(userId);
        opportunity.setUpdateTime(System.currentTimeMillis());
        opportunityMapper.update(opportunity);
    }

    // ========== 私有方法 ==========

    /**
     * 将商机列表转换为 OpportunityListResponse 列表
     *
     * @param opportunities 商机列表
     * @return 响应列表
     */
    private List<OpportunityListResponse> convertToListResponse(List<Opportunity> opportunities) {
        if (opportunities == null || opportunities.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> opportunityIds = opportunities.stream().map(Opportunity::getId).toList();

        // 批量获取自定义字段值
        Map<String, List<ModuleFieldValueDTO>> fieldValuesMap =
                moduleFieldValueService.batchGetFieldValues(FormKey.OPPORTUNITY.getKey(), opportunityIds);

        // 收集所有需要查询名称的 ID
        Set<String> customerIds = new HashSet<>();
        Set<String> contactIds = new HashSet<>();
        Set<String> userIds = new HashSet<>();
        for (Opportunity opp : opportunities) {
            if (StringUtils.isNotBlank(opp.getCustomerId())) {
                customerIds.add(opp.getCustomerId());
            }
            if (StringUtils.isNotBlank(opp.getContactId())) {
                contactIds.add(opp.getContactId());
            }
            if (StringUtils.isNotBlank(opp.getOwner())) {
                userIds.add(opp.getOwner());
            }
        }

        // 批量查询客户名称
        Map<String, String> customerNameMap = Collections.emptyMap();
        if (!customerIds.isEmpty()) {
            List<Customer> customers = customerMapper.selectByIds(new ArrayList<>(customerIds));
            customerNameMap = customers.stream()
                    .filter(c -> StringUtils.isNotBlank(c.getName()))
                    .collect(Collectors.toMap(Customer::getId, Customer::getName, (a, b) -> a));
        }

        // 批量查询联系人名称
        Map<String, String> contactNameMap = Collections.emptyMap();
        if (!contactIds.isEmpty()) {
            List<CustomerContact> contacts = customerContactMapper.selectByIds(new ArrayList<>(contactIds));
            contactNameMap = contacts.stream()
                    .filter(c -> StringUtils.isNotBlank(c.getName()))
                    .collect(Collectors.toMap(CustomerContact::getId, CustomerContact::getName, (a, b) -> a));
        }

        // 批量查询用户姓名
        Map<String, String> userNameMap = Collections.emptyMap();
        if (!userIds.isEmpty()) {
            List<User> users = userMapper.selectByIds(new ArrayList<>(userIds));
            userNameMap = users.stream()
                    .filter(u -> StringUtils.isNotBlank(u.getName()))
                    .collect(Collectors.toMap(User::getId, User::getName, (a, b) -> a));
        }

        Map<String, String> finalCustomerNameMap = customerNameMap;
        Map<String, String> finalContactNameMap = contactNameMap;
        Map<String, String> finalUserNameMap = userNameMap;

        return opportunities.stream()
                .map(opp -> {
                    OpportunityListResponse response = BeanUtils.copyBean(new OpportunityListResponse(), opp);
                    // 设置自定义字段值
                    response.setModuleFields(
                            fieldValuesMap.getOrDefault(opp.getId(), Collections.emptyList()));
                    // 设置客户名称
                    if (StringUtils.isNotBlank(opp.getCustomerId()) && finalCustomerNameMap.containsKey(opp.getCustomerId())) {
                        response.setCustomerName(finalCustomerNameMap.get(opp.getCustomerId()));
                    }
                    // 设置联系人名称
                    if (StringUtils.isNotBlank(opp.getContactId()) && finalContactNameMap.containsKey(opp.getContactId())) {
                        response.setContactName(finalContactNameMap.get(opp.getContactId()));
                    }
                    // 设置负责人姓名
                    if (StringUtils.isNotBlank(opp.getOwner()) && finalUserNameMap.containsKey(opp.getOwner())) {
                        response.setOwnerName(finalUserNameMap.get(opp.getOwner()));
                    }
                    return response;
                })
                .toList();
    }
}
