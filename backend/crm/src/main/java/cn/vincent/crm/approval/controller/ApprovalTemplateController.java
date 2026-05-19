package cn.vincent.crm.approval.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.approval.domain.ApprovalTemplate;
import cn.vincent.crm.approval.dto.request.ApprovalTemplateAddRequest;
import cn.vincent.crm.approval.dto.request.ApprovalTemplateEnableRequest;
import cn.vincent.crm.approval.dto.request.ApprovalTemplatePageRequest;
import cn.vincent.crm.approval.dto.request.ApprovalTemplateUpdateRequest;
import cn.vincent.crm.approval.dto.response.ApprovalTemplateGetResponse;
import cn.vincent.crm.approval.dto.response.ApprovalTemplateListResponse;
import cn.vincent.crm.approval.service.ApprovalTemplateService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 审批模板控制器
 */
@RestController
@RequestMapping("/approval/template")
@Tag(name = "审批模板管理")
public class ApprovalTemplateController {

    @Resource
    private ApprovalTemplateService approvalTemplateService;

    /**
     * 新增审批模板
     *
     * @param request 新增请求
     * @return 新增的审批模板实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.APPROVAL_TEMPLATE_ADD)
    @Operation(summary = "新增审批模板")
    public ApprovalTemplate add(@Validated @RequestBody ApprovalTemplateAddRequest request) {
        return approvalTemplateService.add(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 更新审批模板
     *
     * @param request 更新请求
     * @return 更新后的审批模板实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.APPROVAL_TEMPLATE_UPDATE)
    @Operation(summary = "更新审批模板")
    public ApprovalTemplate update(@Validated @RequestBody ApprovalTemplateUpdateRequest request) {
        return approvalTemplateService.update(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 删除审批模板
     *
     * @param request 删除请求（复用启用请求的 ID 字段）
     */
    @PostMapping("/delete")
    @RequiresPermissions(PermissionConstants.APPROVAL_TEMPLATE_DELETE)
    @Operation(summary = "删除审批模板")
    public void delete(@Validated @RequestBody ApprovalTemplateEnableRequest request) {
        approvalTemplateService.delete(request.getId());
    }

    /**
     * 审批模板分页列表
     *
     * @param request 分页请求
     * @return 分页结果
     */
    @PostMapping("/list")
    @RequiresPermissions(PermissionConstants.APPROVAL_TEMPLATE_READ)
    @Operation(summary = "审批模板列表")
    public PagerWithOption<List<ApprovalTemplateListResponse>> list(@Validated @RequestBody ApprovalTemplatePageRequest request) {
        return approvalTemplateService.list(request, OrganizationContext.getOrganizationId());
    }

    /**
     * 审批模板详情
     *
     * @param id 模板 ID
     * @return 模板详情
     */
    @GetMapping("/get/{id}")
    @RequiresPermissions(PermissionConstants.APPROVAL_TEMPLATE_READ)
    @Operation(summary = "审批模板详情")
    public ApprovalTemplateGetResponse get(@PathVariable String id) {
        return approvalTemplateService.get(id);
    }

    /**
     * 启用/禁用审批模板
     *
     * @param request 启用/禁用请求
     */
    @PostMapping("/enable")
    @RequiresPermissions(PermissionConstants.APPROVAL_TEMPLATE_UPDATE)
    @Operation(summary = "启用/禁用审批模板")
    public void enable(@Validated @RequestBody ApprovalTemplateEnableRequest request) {
        approvalTemplateService.enable(request, SessionUtils.getUserId());
    }
}
