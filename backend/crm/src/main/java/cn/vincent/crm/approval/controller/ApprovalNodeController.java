package cn.vincent.crm.approval.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.crm.approval.dto.request.ApprovalOperateRequest;
import cn.vincent.crm.approval.service.ApprovalInstanceService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 审批节点操作控制器
 */
@RestController
@RequestMapping("/approval/node")
@Tag(name = "审批节点操作")
public class ApprovalNodeController {

    @Resource
    private ApprovalInstanceService approvalInstanceService;

    /**
     * 审批通过
     *
     * @param request 审批操作请求
     */
    @PostMapping("/approve")
    @RequiresPermissions(PermissionConstants.APPROVAL_INSTANCE_SUBMIT)
    @Operation(summary = "审批通过")
    public void approve(@Validated @RequestBody ApprovalOperateRequest request) {
        approvalInstanceService.approve(request, SessionUtils.getUserId());
    }

    /**
     * 审批驳回
     *
     * @param request 审批操作请求
     */
    @PostMapping("/reject")
    @RequiresPermissions(PermissionConstants.APPROVAL_INSTANCE_SUBMIT)
    @Operation(summary = "审批驳回")
    public void reject(@Validated @RequestBody ApprovalOperateRequest request) {
        approvalInstanceService.reject(request, SessionUtils.getUserId());
    }
}
