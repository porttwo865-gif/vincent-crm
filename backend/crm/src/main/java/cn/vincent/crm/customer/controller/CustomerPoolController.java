package cn.vincent.crm.customer.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.customer.dto.request.CustomerPageRequest;
import cn.vincent.crm.customer.dto.request.CustomerPoolAssignRequest;
import cn.vincent.crm.customer.dto.request.CustomerPoolClaimRequest;
import cn.vincent.crm.customer.dto.response.CustomerListResponse;
import cn.vincent.crm.customer.service.CustomerPoolService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公海池控制器
 */
@RestController
@RequestMapping("/customer-pool")
@Tag(name = "公海池管理")
public class CustomerPoolController {

    /** 公海池服务 */
    @Resource
    private CustomerPoolService customerPoolService;

    /**
     * 公海池客户列表
     *
     * @param request 分页请求
     * @return 分页结果
     */
    @PostMapping("/page")
    @RequiresPermissions(PermissionConstants.CUSTOMER_POOL_READ)
    @Operation(summary = "公海池客户列表")
    public PagerWithOption<List<CustomerListResponse>> list(@Validated @RequestBody CustomerPageRequest request) {
        return customerPoolService.list(request, OrganizationContext.getOrganizationId());
    }

    /**
     * 领取公海池客户
     *
     * @param request 领取请求
     */
    @PostMapping("/claim")
    @RequiresPermissions(PermissionConstants.CUSTOMER_POOL_CLAIM)
    @Operation(summary = "领取客户")
    public void claim(@Validated @RequestBody CustomerPoolClaimRequest request) {
        customerPoolService.claim(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 分配公海池客户
     *
     * @param request 分配请求
     */
    @PostMapping("/assign")
    @RequiresPermissions(PermissionConstants.CUSTOMER_POOL_ASSIGN)
    @Operation(summary = "分配客户")
    public void assign(@Validated @RequestBody CustomerPoolAssignRequest request) {
        customerPoolService.assign(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }
}
