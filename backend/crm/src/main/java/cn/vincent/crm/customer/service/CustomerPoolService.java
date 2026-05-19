package cn.vincent.crm.customer.service;

import cn.vincent.common.constants.FormKey;
import cn.vincent.common.exception.GenericException;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.ConditionFilterUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.customer.constants.CustomerConstants;
import cn.vincent.crm.customer.domain.Customer;
import cn.vincent.crm.customer.dto.request.CustomerPageRequest;
import cn.vincent.crm.customer.dto.request.CustomerPoolAssignRequest;
import cn.vincent.crm.customer.dto.request.CustomerPoolClaimRequest;
import cn.vincent.crm.customer.dto.response.CustomerListResponse;
import cn.vincent.crm.customer.mapper.CustomerMapper;
import cn.vincent.crm.customer.mapper.CustomerOwnerMapper;
import cn.vincent.crm.customer.mapper.ExtCustomerMapper;
import cn.vincent.crm.system.domain.User;
import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import cn.vincent.crm.system.mapper.UserMapper;
import cn.vincent.crm.system.service.ModuleFieldValueService;
import cn.vincent.common.response.PagerWithOption;
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
 * 公海池服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class CustomerPoolService {

    /** 客户通用 Mapper */
    @Resource
    private CustomerMapper customerMapper;

    /** 客户自定义 Mapper */
    @Resource
    private ExtCustomerMapper extCustomerMapper;

    /** 客户负责人变更历史 Mapper */
    @Resource
    private CustomerOwnerMapper customerOwnerMapper;

    /** 用户 Mapper */
    @Resource
    private UserMapper userMapper;

    /** 自定义字段值服务 */
    @Resource
    private ModuleFieldValueService moduleFieldValueService;

    /**
     * 公海池客户列表（分页）
     *
     * @param request  分页请求
     * @param orgId    当前组织 ID
     * @return 分页结果
     */
    public PagerWithOption<List<CustomerListResponse>> list(CustomerPageRequest request, String orgId) {
        // 解析条件筛选
        ConditionFilterUtils.parseCondition(request, FormKey.CUSTOMER.getKey());

        // 公海池固定 inSharedPool = true
        request.setInSharedPool(true);

        PageHelper.startPage(request.getCurrent(), request.getPageSize());
        List<Customer> customers = extCustomerMapper.selectCustomerPage(orgId, null, true);
        PageInfo<Customer> pageInfo = new PageInfo<>(customers);

        // 转换为响应 DTO
        List<String> customerIds = customers.stream().map(Customer::getId).toList();

        // 批量查询自定义字段值
        Map<String, List<ModuleFieldValueDTO>> fieldValuesMap = Collections.emptyMap();
        if (!customerIds.isEmpty()) {
            fieldValuesMap = moduleFieldValueService.batchGetFieldValues(
                    FormKey.CUSTOMER.getKey(), customerIds);
        }

        // 收集需要查询姓名的用户 ID
        Set<String> allUserIds = new HashSet<>();
        for (Customer c : customers) {
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
                    response.setModuleFields(
                            finalFieldValuesMap.getOrDefault(customer.getId(), Collections.emptyList()));
                    if (StringUtils.isNotBlank(customer.getCreateUser())
                            && finalUserNameMap.containsKey(customer.getCreateUser())) {
                        response.setCreateUserName(finalUserNameMap.get(customer.getCreateUser()));
                    }
                    return response;
                })
                .toList();

        return PagerWithOption.of(responseList, pageInfo.getTotal(),
                request.getCurrent(), request.getPageSize());
    }

    /**
     * 领取公海池客户
     *
     * @param request 领取请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     */
    public void claim(CustomerPoolClaimRequest request, String userId, String orgId) {
        List<String> ids = request.getIds();
        if (ids == null || ids.isEmpty()) {
            return;
        }

        long now = System.currentTimeMillis();
        for (String id : ids) {
            Customer customer = customerMapper.selectByPrimaryKey(id);
            if (customer == null) {
                continue;
            }
            String fromOwner = customer.getOwner();
            customer.setOwner(userId);
            customer.setInSharedPool(false);
            customer.setCollectionTime(now);
            customer.setUpdateUser(userId);
            customer.setUpdateTime(now);
            customerMapper.update(customer);

            // 记录负责人变更历史
            saveOwnerHistory(id, fromOwner, userId,
                    CustomerConstants.OPERATION_TYPE_CLAIM, userId, orgId);
        }
    }

    /**
     * 分配公海池客户
     *
     * @param request 分配请求
     * @param userId  当前用户 ID（操作人）
     * @param orgId   当前组织 ID
     */
    public void assign(CustomerPoolAssignRequest request, String userId, String orgId) {
        List<String> ids = request.getIds();
        if (ids == null || ids.isEmpty()) {
            return;
        }

        String targetOwner = request.getOwnerId();
        long now = System.currentTimeMillis();

        for (String id : ids) {
            Customer customer = customerMapper.selectByPrimaryKey(id);
            if (customer == null) {
                continue;
            }
            String fromOwner = customer.getOwner();
            customer.setOwner(targetOwner);
            customer.setInSharedPool(false);
            customer.setCollectionTime(now);
            customer.setUpdateUser(userId);
            customer.setUpdateTime(now);
            customerMapper.update(customer);

            // 记录负责人变更历史
            saveOwnerHistory(id, fromOwner, targetOwner,
                    CustomerConstants.OPERATION_TYPE_ASSIGN, userId, orgId);
        }
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
        cn.vincent.crm.customer.domain.CustomerOwner ownerHistory =
                new cn.vincent.crm.customer.domain.CustomerOwner();
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
