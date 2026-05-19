package cn.vincent.crm.approval.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.approval.domain.ApprovalInstance;
import cn.vincent.crm.approval.dto.request.ApprovalCancelRequest;
import cn.vincent.crm.approval.dto.request.ApprovalInstancePageRequest;
import cn.vincent.crm.approval.dto.request.ApprovalSubmitRequest;
import cn.vincent.crm.approval.dto.response.ApprovalInstanceGetResponse;
import cn.vincent.crm.approval.dto.response.ApprovalInstanceListResponse;
import cn.vincent.crm.approval.service.ApprovalInstanceService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 审批实例控制器
 */
@RestController
@RequestMapping("/approval/instance")
@Tag(name = "审批实例管理")
public class ApprovalInstanceController {

    @Resource
    private ApprovalInstanceService approvalInstanceService;

    /**
     * 发起审批
     *
     * @param request 发起审批请求
     * @return 审批实例
     */
    @PostMapping("/submit")
    @RequiresPermissions(PermissionConstants.APPROVAL_INSTANCE_SUBMIT)
    @Operation(summary = "发起审批")
    public ApprovalInstance submit(@Validated @RequestBody ApprovalSubmitRequest request) {
        return approvalInstanceService.submit(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 撤回审批
     *
     * @param request 撤回请求
     */
    @PostMapping("/cancel")
    @RequiresPermissions(PermissionConstants.APPROVAL_INSTANCE_SUBMIT)
    @Operation(summary = "撤回审批")
    public void cancel(@Validated @RequestBody ApprovalCancelRequest request) {
        approvalInstanceService.cancel(request, SessionUtils.getUserId());
    }

    /**
     * 审批实例分页列表
     *
     * @param request 分页请求
     * @return 分页结果
     */
    @PostMapping("/list")
    @RequiresPermissions(PermissionConstants.APPROVAL_INSTANCE_READ)
    @Operation(summary = "审批实例列表")
    public PagerWithOption<List<ApprovalInstanceListResponse>> list(@Validated @RequestBody ApprovalInstancePageRequest request) {
        return approvalInstanceService.list(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 审批实例详情
     *
     * @param id 实例 ID
     * @return 实例详情
     */
    @GetMapping("/get/{id}")
    @RequiresPermissions(PermissionConstants.APPROVAL_INSTANCE_READ)
    @Operation(summary = "审批实例详情")
    public ApprovalInstanceGetResponse get(@PathVariable String id) {
        return approvalInstanceService.get(id);
    }
}
