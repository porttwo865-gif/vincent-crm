package cn.vincent.crm.order.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.FormKey;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.order.domain.Order;
import cn.vincent.crm.order.dto.request.OrderAddRequest;
import cn.vincent.crm.order.dto.request.OrderPageRequest;
import cn.vincent.crm.order.dto.request.OrderStatusRequest;
import cn.vincent.crm.order.dto.request.OrderUpdateRequest;
import cn.vincent.crm.order.dto.response.OrderGetResponse;
import cn.vincent.crm.order.dto.response.OrderListResponse;
import cn.vincent.crm.order.service.OrderService;
import cn.vincent.crm.system.dto.response.ModuleFormConfigDTO;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.security.SessionUtils;
import cn.vincent.crm.system.service.ModuleFormCacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/order")
@Tag(name = "订单管理")
public class OrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private ModuleFormCacheService moduleFormCacheService;

    /**
     * 获取订单表单配置
     *
     * @return 表单配置
     */
    @GetMapping("/module/form")
    @RequiresPermissions(PermissionConstants.ORDER_MANAGEMENT_READ)
    @Operation(summary = "获取订单表单配置")
    public ModuleFormConfigDTO getModuleFormConfig() {
        return moduleFormCacheService.getBusinessFormConfig(
                FormKey.ORDER.getKey(), OrganizationContext.getOrganizationId());
    }

    /**
     * 订单列表
     *
     * @param request 分页请求
     * @return 分页结果
     */
    @PostMapping("/page")
    @RequiresPermissions(PermissionConstants.ORDER_MANAGEMENT_READ)
    @Operation(summary = "订单列表")
    public PagerWithOption<List<OrderListResponse>> list(@Validated @RequestBody OrderPageRequest request) {
        return orderService.list(request, OrganizationContext.getOrganizationId());
    }

    /**
     * 新增订单
     *
     * @param request 新增请求
     * @return 新增的订单实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.ORDER_MANAGEMENT_ADD)
    @Operation(summary = "新增订单")
    public Order add(@Validated @RequestBody OrderAddRequest request) {
        return orderService.add(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 更新订单
     *
     * @param request 更新请求
     * @return 更新后的订单实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.ORDER_MANAGEMENT_UPDATE)
    @Operation(summary = "更新订单")
    public Order update(@Validated @RequestBody OrderUpdateRequest request) {
        return orderService.update(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 删除订单
     *
     * @param id 订单 ID
     */
    @GetMapping("/delete/{id}")
    @RequiresPermissions(PermissionConstants.ORDER_MANAGEMENT_DELETE)
    @Operation(summary = "删除订单")
    public void delete(@PathVariable String id) {
        orderService.delete(id);
    }

    /**
     * 订单详情
     *
     * @param id 订单 ID
     * @return 订单详情
     */
    @GetMapping("/get/{id}")
    @RequiresPermissions(PermissionConstants.ORDER_MANAGEMENT_READ)
    @Operation(summary = "订单详情")
    public OrderGetResponse get(@PathVariable String id) {
        return orderService.getWithDataPermissionCheck(
                id, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 变更订单状态
     *
     * @param request 状态变更请求
     * @return 更新后的订单实体
     */
    @PostMapping("/status")
    @RequiresPermissions(PermissionConstants.ORDER_MANAGEMENT_UPDATE)
    @Operation(summary = "变更订单状态")
    public Order changeStatus(@Validated @RequestBody OrderStatusRequest request) {
        return orderService.changeStatus(request, SessionUtils.getUserId());
    }
}
