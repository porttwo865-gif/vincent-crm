package cn.vincent.crm.customer.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.FormKey;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.customer.domain.Customer;
import cn.vincent.crm.customer.dto.request.CustomerAddRequest;
import cn.vincent.crm.customer.dto.request.CustomerMovePoolRequest;
import cn.vincent.crm.customer.dto.request.CustomerPageRequest;
import cn.vincent.crm.customer.dto.request.CustomerUpdateRequest;
import cn.vincent.crm.customer.dto.response.CustomerContractStatisticResponse;
import cn.vincent.crm.customer.dto.response.CustomerGetResponse;
import cn.vincent.crm.customer.dto.response.CustomerListResponse;
import cn.vincent.crm.customer.service.CustomerService;
import cn.vincent.crm.system.dto.response.ModuleFormConfigDTO;
import cn.vincent.crm.system.service.ModuleFormCacheService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户管理控制器
 */
@RestController
@RequestMapping("/account")
@Tag(name = "客户管理")
public class CustomerController {

    /** 客户服务 */
    @Resource
    private CustomerService customerService;

    /** 模块表单缓存服务 */
    @Resource
    private ModuleFormCacheService moduleFormCacheService;

    /**
     * 获取客户表单配置
     *
     * @return 表单配置
     */
    @GetMapping("/module/form")
    @RequiresPermissions(PermissionConstants.CUSTOMER_MANAGEMENT_READ)
    @Operation(summary = "获取客户表单配置")
    public ModuleFormConfigDTO getModuleFormConfig() {
        return moduleFormCacheService.getBusinessFormConfig(
                FormKey.CUSTOMER.getKey(), OrganizationContext.getOrganizationId());
    }

    /**
     * 客户列表
     *
     * @param request 分页请求
     * @return 分页结果
     */
    @PostMapping("/page")
    @RequiresPermissions(PermissionConstants.CUSTOMER_MANAGEMENT_READ)
    @Operation(summary = "客户列表")
    public PagerWithOption<List<CustomerListResponse>> list(@Validated @RequestBody CustomerPageRequest request) {
        return customerService.list(request, SessionUtils.getUserId(),
                OrganizationContext.getOrganizationId(), null);
    }

    /**
     * 新增客户
     *
     * @param request 新增请求
     * @return 新增的客户实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.CUSTOMER_MANAGEMENT_ADD)
    @Operation(summary = "新增客户")
    public Customer add(@Validated @RequestBody CustomerAddRequest request) {
        return customerService.add(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 更新客户
     *
     * @param request 更新请求
     * @return 更新后的客户实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.CUSTOMER_MANAGEMENT_UPDATE)
    @Operation(summary = "更新客户")
    public Customer update(@Validated @RequestBody CustomerUpdateRequest request) {
        return customerService.update(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 删除客户
     *
     * @param id 客户 ID
     */
    @GetMapping("/delete/{id}")
    @RequiresPermissions(PermissionConstants.CUSTOMER_MANAGEMENT_DELETE)
    @Operation(summary = "删除客户")
    public void delete(@PathVariable String id) {
        customerService.delete(id);
    }

    /**
     * 客户详情
     *
     * @param id 客户 ID
     * @return 客户详情
     */
    @GetMapping("/get/{id}")
    @RequiresPermissions(PermissionConstants.CUSTOMER_MANAGEMENT_READ)
    @Operation(summary = "客户详情")
    public CustomerGetResponse get(@PathVariable String id) {
        return customerService.getWithDataPermissionCheck(
                id, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 批量移入公海
     *
     * @param request 移入公海请求
     */
    @PostMapping("/batch/move-pool")
    @RequiresPermissions(PermissionConstants.CUSTOMER_MANAGEMENT_RECYCLE)
    @Operation(summary = "批量移入公海")
    public void moveToPool(@Validated @RequestBody CustomerMovePoolRequest request) {
        customerService.moveToPool(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 客户合同统计
     *
     * @param id 客户 ID
     * @return 合同统计
     */
    @GetMapping("/{id}/contract-statistic")
    @RequiresPermissions(PermissionConstants.CUSTOMER_MANAGEMENT_READ)
    @Operation(summary = "客户合同统计")
    public CustomerContractStatisticResponse getContractStatistic(@PathVariable String id) {
        return customerService.getContractStatistic(id);
    }
}
