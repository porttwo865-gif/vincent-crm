package cn.vincent.crm.customer.service;

import cn.vincent.common.constants.FormKey;
import cn.vincent.common.exception.GenericException;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.ConditionFilterUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.customer.constants.CustomerConstants;
import cn.vincent.crm.customer.domain.Customer;
import cn.vincent.crm.customer.domain.CustomerContact;
import cn.vincent.crm.customer.domain.CustomerOwner;
import cn.vincent.crm.customer.dto.request.CustomerAddRequest;
import cn.vincent.crm.customer.dto.request.CustomerMovePoolRequest;
import cn.vincent.crm.customer.dto.request.CustomerPageRequest;
import cn.vincent.crm.customer.dto.request.CustomerUpdateRequest;
import cn.vincent.crm.customer.dto.response.ContactListResponse;
import cn.vincent.crm.customer.dto.response.CustomerContractStatisticResponse;
import cn.vincent.crm.customer.dto.response.CustomerGetResponse;
import cn.vincent.crm.customer.dto.response.CustomerListResponse;
import cn.vincent.crm.customer.mapper.CustomerContactMapper;
import cn.vincent.crm.customer.mapper.CustomerMapper;
import cn.vincent.crm.customer.mapper.CustomerOwnerMapper;
import cn.vincent.crm.customer.mapper.ExtCustomerContactMapper;
import cn.vincent.crm.customer.mapper.ExtCustomerMapper;
import cn.vincent.crm.customer.mapper.ExtCustomerOwnerMapper;
import cn.vincent.crm.system.domain.User;
import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import cn.vincent.crm.system.mapper.UserMapper;
import cn.vincent.crm.system.service.BaseService;
import cn.vincent.crm.system.service.ModuleFieldValueService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 客户管理服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class CustomerService {

    /** 客户通用 Mapper */
    @Resource
    private CustomerMapper customerMapper;

    /** 客户自定义 Mapper */
    @Resource
    private ExtCustomerMapper extCustomerMapper;

    /** 客户联系人 Mapper */
    @Resource
    private CustomerContactMapper customerContactMapper;

    /** 客户联系人自定义 Mapper */
    @Resource
    private ExtCustomerContactMapper extCustomerContactMapper;

    /** 客户负责人变更历史 Mapper */
    @Resource
    private CustomerOwnerMapper customerOwnerMapper;

    /** 客户负责人变更历史自定义 Mapper */
    @Resource
    private ExtCustomerOwnerMapper extCustomerOwnerMapper;

    /** 用户 Mapper */
    @Resource
    private UserMapper userMapper;

    /** 通用基础服务 */
    @Resource
    private BaseService baseService;

    /** 自定义字段值服务 */
    @Resource
    private ModuleFieldValueService moduleFieldValueService;

    /**
     * 新增客户
     *
     * @param request 新增请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 新增的客户实体
     */
    public Customer add(CustomerAddRequest request, String userId, String orgId) {
        Customer customer = new Customer();
        customer.setId(IDGenerator.nextStr());
        customer.setName(request.getName());
        customer.setOwner(StringUtils.isNotBlank(request.getOwner()) ? request.getOwner() : userId);
        customer.setInSharedPool(false);
        customer.setOrganizationId(orgId);
        customer.setCollectionTime(System.currentTimeMillis());
        customer.setCreateUser(userId);
        customer.setUpdateUser(userId);
        customer.setCreateTime(System.currentTimeMillis());
        customer.setUpdateTime(System.currentTimeMillis());
        customerMapper.insert(customer);

        // 保存自定义字段值
        moduleFieldValueService.saveFieldValues(
                FormKey.CUSTOMER.getKey(), customer.getId(), request.getModuleFields(), userId);

        // 记录负责人变更历史
        saveOwnerHistory(customer.getId(), null, customer.getOwner(),
                CustomerConstants.OPERATION_TYPE_CLAIM, userId, orgId);

        return customer;
    }

    /**
     * 更新客户
     *
     * @param request 更新请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 更新后的客户实体
     */
    public Customer update(CustomerUpdateRequest request, String userId, String orgId) {
        Customer customer = customerMapper.selectByPrimaryKey(request.getId());
        if (customer == null) {
            throw new GenericException(Translator.get("customer.not.exist"));
        }

        if (request.getName() != null) {
            customer.setName(request.getName());
        }
        customer.setUpdateUser(userId);
        customer.setUpdateTime(System.currentTimeMillis());
        customerMapper.update(customer);

        // 保存自定义字段值
        moduleFieldValueService.saveFieldValues(
                FormKey.CUSTOMER.getKey(), customer.getId(), request.getModuleFields(), userId);

        return customer;
    }

    /**
     * 删除客户
     *
     * @param id 客户 ID
     */
    public void delete(String id) {
        Customer customer = customerMapper.selectByPrimaryKey(id);
        if (customer == null) {
            throw new GenericException(Translator.get("customer.not.exist"));
        }

        // 删除自定义字段值
        moduleFieldValueService.deleteFieldValues(FormKey.CUSTOMER.getKey(), id);

        // 删除关联联系人
        List<CustomerContact> contacts = extCustomerContactMapper.selectByCustomerId(id);
        if (contacts != null && !contacts.isEmpty()) {
            List<String> contactIds = contacts.stream().map(CustomerContact::getId).toList();
            customerContactMapper.deleteByIds(contactIds);
        }

        // 删除客户
        customerMapper.deleteByIds(List.of(id));
    }

    /**
     * 客户列表（分页）
     *
     * @param request  分页请求
     * @param userId   当前用户 ID
     * @param orgId    当前组织 ID
     * @param ownerIds 数据权限范围内的负责人 ID 列表
     * @return 分页结果
     */
    public PagerWithOption<List<CustomerListResponse>> list(CustomerPageRequest request,
                                                            String userId, String orgId,
                                                            List<String> ownerIds) {
        // 解析条件筛选
        ConditionFilterUtils.parseCondition(request, FormKey.CUSTOMER.getKey());

        PageHelper.startPage(request.getCurrent(), request.getPageSize());
        List<Customer> customers = extCustomerMapper.selectCustomerPage(
                orgId, ownerIds, request.getInSharedPool());
        PageInfo<Customer> pageInfo = new PageInfo<>(customers);

        // 转换为响应 DTO
        List<String> customerIds = customers.stream().map(Customer::getId).toList();

        // 批量查询自定义字段值
        Map<String, List<ModuleFieldValueDTO>> fieldValuesMap = Collections.emptyMap();
        if (!customerIds.isEmpty()) {
            fieldValuesMap = moduleFieldValueService.batchGetFieldValues(
                    FormKey.CUSTOMER.getKey(), customerIds);
        }

        // 收集所有需要查询姓名的用户 ID
        Set<String> allUserIds = new HashSet<>();
        for (Customer c : customers) {
            if (StringUtils.isNotBlank(c.getOwner())) {
                allUserIds.add(c.getOwner());
            }
            if (StringUtils.isNotBlank(c.getFollower())) {
                allUserIds.add(c.getFollower());
            }
            if (StringUtils.isNotBlank(c.getCreateUser())) {
                allUserIds.add(c.getCreateUser());
            }
        }

        // 批量查询用户姓名
        Map<String, String> userNameMap = Collections.emptyMap();
        if (!allUserIds.isEmpty()) {
            List<User> users = userMapper.selectByIds(new ArrayList<>(allUserIds));
            userNameMap = users.stream()
                    .filter(u -> StringUtils.isNotBlank(u.getName()))
                    .collect(Collectors.toMap(User::getId, User::getName, (a, b) -> a));
        }

        Map<String, List<ModuleFieldValueDTO>> finalFieldValuesMap = fieldValuesMap;
        Map<String, String> finalUserNameMap = userNameMap;

        List<CustomerListResponse> responseList = customers.stream()
                .map(customer -> {
                    CustomerListResponse response = BeanUtils.copyBean(new CustomerListResponse(), customer);
                    // 设置自定义字段值
                    response.setModuleFields(
                            finalFieldValuesMap.getOrDefault(customer.getId(), Collections.emptyList()));
                    // 设置姓名
                    if (StringUtils.isNotBlank(customer.getOwner()) && finalUserNameMap.containsKey(customer.getOwner())) {
                        response.setOwnerName(finalUserNameMap.get(customer.getOwner()));
                    }
                    if (StringUtils.isNotBlank(customer.getFollower()) && finalUserNameMap.containsKey(customer.getFollower())) {
                        response.setFollowerName(finalUserNameMap.get(customer.getFollower()));
                    }
                    if (StringUtils.isNotBlank(customer.getCreateUser()) && finalUserNameMap.containsKey(customer.getCreateUser())) {
                        response.setCreateUserName(finalUserNameMap.get(customer.getCreateUser()));
                    }
                    return response;
                })
                .toList();

        return PagerWithOption.of(responseList, pageInfo.getTotal(),
                request.getCurrent(), request.getPageSize());
    }

    /**
     * 客户详情（含数据权限校验）
     *
     * @param id     客户 ID
     * @param userId 当前用户 ID
     * @param orgId  当前组织 ID
     * @return 客户详情响应
     */
    public CustomerGetResponse getWithDataPermissionCheck(String id, String userId, String orgId) {
        CustomerGetResponse response = get(id);
        if (response == null) {
            throw new GenericException(Translator.get("customer.not.exist"));
        }
        return response;
    }

    /**
     * 客户详情
     *
     * @param id 客户 ID
     * @return 客户详情响应
     */
    public CustomerGetResponse get(String id) {
        Customer customer = customerMapper.selectByPrimaryKey(id);
        if (customer == null) {
            return null;
        }

        CustomerGetResponse response = BeanUtils.copyBean(new CustomerGetResponse(), customer);

        // 查询自定义字段值
        List<ModuleFieldValueDTO> fieldValues = moduleFieldValueService.getFieldValues(
                FormKey.CUSTOMER.getKey(), id);
        response.setModuleFields(fieldValues);

        // 设置创建人/更新人/负责人姓名
        baseService.setCreateUpdateOwnerUserName(response);

        // 设置跟进人姓名
        if (StringUtils.isNotBlank(customer.getFollower())) {
            User follower = userMapper.selectByPrimaryKey(customer.getFollower());
            if (follower != null) {
                response.setFollowerName(follower.getName());
            }
        }

        // 查询联系人列表
        List<CustomerContact> contacts = extCustomerContactMapper.selectByCustomerId(id);
        List<ContactListResponse> contactResponses = contacts.stream()
                .map(contact -> BeanUtils.copyBean(new ContactListResponse(), contact))
                .toList();
        response.setContacts(contactResponses);

        // 查询负责人变更历史
        List<CustomerOwner> ownerHistory = extCustomerOwnerMapper.selectByCustomerId(id);
        response.setOwnerHistory(ownerHistory);

        return response;
    }

    /**
     * 批量移入公海池
     *
     * @param request 移入公海请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     */
    public void moveToPool(CustomerMovePoolRequest request, String userId, String orgId) {
        List<String> ids = request.getIds();
        if (ids == null || ids.isEmpty()) {
            return;
        }

        // 先查询客户信息用于记录历史
        List<Customer> customers = customerMapper.selectByIds(ids);

        // 批量更新客户状态
        extCustomerMapper.batchMoveToPool(ids, request.getReasonId(),
                userId, System.currentTimeMillis());

        // 记录负责人变更历史
        for (Customer customer : customers) {
            saveOwnerHistory(customer.getId(), customer.getOwner(), null,
                    CustomerConstants.OPERATION_TYPE_MOVE_POOL, userId, orgId);
        }
    }

    /**
     * 获取客户合同统计（占位实现）
     *
     * @param id 客户 ID
     * @return 合同统计响应
     */
    public CustomerContractStatisticResponse getContractStatistic(String id) {
        // 暂时返回空统计（合同模块后续实现），结构占位
        return new CustomerContractStatisticResponse();
    }

    /**
     * 保存负责人变更历史
     *
     * @param customerId    客户 ID
     * @param fromOwner     原负责人
     * @param toOwner       新负责人
     * @param operationType 操作类型
     * @param operatorId    操作人
     * @param orgId         组织 ID
     */
    private void saveOwnerHistory(String customerId, String fromOwner, String toOwner,
                                  String operationType, String operatorId, String orgId) {
        CustomerOwner ownerHistory = new CustomerOwner();
        ownerHistory.setId(IDGenerator.nextStr());
        ownerHistory.setCustomerId(customerId);
        ownerHistory.setFromOwner(fromOwner);
        ownerHistory.setToOwner(toOwner);
        ownerHistory.setOperationType(operationType);
        ownerHistory.setOperatorId(operatorId);
        ownerHistory.setOperateTime(System.currentTimeMillis());
        ownerHistory.setOrganizationId(orgId);
        customerOwnerMapper.insert(ownerHistory);
    }
}
