package cn.vincent.crm.invoice.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.FormKey;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.invoice.domain.Invoice;
import cn.vincent.crm.invoice.dto.request.InvoiceAddRequest;
import cn.vincent.crm.invoice.dto.request.InvoicePageRequest;
import cn.vincent.crm.invoice.dto.request.InvoiceStatusRequest;
import cn.vincent.crm.invoice.dto.request.InvoiceUpdateRequest;
import cn.vincent.crm.invoice.dto.response.InvoiceGetResponse;
import cn.vincent.crm.invoice.dto.response.InvoiceListResponse;
import cn.vincent.crm.invoice.service.InvoiceService;
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
 * 发票控制器
 */
@RestController
@RequestMapping("/invoice")
@Tag(name = "发票管理")
public class InvoiceController {

    @Resource
    private InvoiceService invoiceService;

    @Resource
    private ModuleFormCacheService moduleFormCacheService;

    /**
     * 获取发票表单配置
     *
     * @return 表单配置
     */
    @GetMapping("/module/form")
    @RequiresPermissions(PermissionConstants.INVOICE_MANAGEMENT_READ)
    @Operation(summary = "获取发票表单配置")
    public ModuleFormConfigDTO getModuleFormConfig() {
        return moduleFormCacheService.getBusinessFormConfig(
                FormKey.INVOICE.getKey(), OrganizationContext.getOrganizationId());
    }

    /**
     * 发票列表
     *
     * @param request 分页请求
     * @return 分页结果
     */
    @PostMapping("/page")
    @RequiresPermissions(PermissionConstants.INVOICE_MANAGEMENT_READ)
    @Operation(summary = "发票列表")
    public PagerWithOption<List<InvoiceListResponse>> list(@Validated @RequestBody InvoicePageRequest request) {
        return invoiceService.list(request, OrganizationContext.getOrganizationId());
    }

    /**
     * 新增发票
     *
     * @param request 新增请求
     * @return 新增的发票实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.INVOICE_MANAGEMENT_ADD)
    @Operation(summary = "新增发票")
    public Invoice add(@Validated @RequestBody InvoiceAddRequest request) {
        return invoiceService.add(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 更新发票
     *
     * @param request 更新请求
     * @return 更新后的发票实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.INVOICE_MANAGEMENT_UPDATE)
    @Operation(summary = "更新发票")
    public Invoice update(@Validated @RequestBody InvoiceUpdateRequest request) {
        return invoiceService.update(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 删除发票
     *
     * @param id 发票 ID
     */
    @GetMapping("/delete/{id}")
    @RequiresPermissions(PermissionConstants.INVOICE_MANAGEMENT_DELETE)
    @Operation(summary = "删除发票")
    public void delete(@PathVariable String id) {
        invoiceService.delete(id);
    }

    /**
     * 发票详情
     *
     * @param id 发票 ID
     * @return 发票详情
     */
    @GetMapping("/get/{id}")
    @RequiresPermissions(PermissionConstants.INVOICE_MANAGEMENT_READ)
    @Operation(summary = "发票详情")
    public InvoiceGetResponse get(@PathVariable String id) {
        return invoiceService.getWithDataPermissionCheck(
                id, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 变更发票状态
     *
     * @param request 状态变更请求
     * @return 更新后的发票实体
     */
    @PostMapping("/status")
    @RequiresPermissions(PermissionConstants.INVOICE_MANAGEMENT_UPDATE)
    @Operation(summary = "变更发票状态")
    public Invoice changeStatus(@Validated @RequestBody InvoiceStatusRequest request) {
        return invoiceService.changeStatus(request, SessionUtils.getUserId());
    }
}
