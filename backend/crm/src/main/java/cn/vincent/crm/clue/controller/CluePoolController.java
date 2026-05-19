package cn.vincent.crm.clue.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.clue.dto.request.CluePageRequest;
import cn.vincent.crm.clue.dto.request.CluePoolAssignRequest;
import cn.vincent.crm.clue.dto.request.CluePoolClaimRequest;
import cn.vincent.crm.clue.dto.response.ClueListResponse;
import cn.vincent.crm.clue.service.CluePoolService;
import cn.vincent.security.DataScopeService;
import cn.vincent.security.SessionUtils;
import cn.vincent.security.dto.DeptDataPermissionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 线索池控制器
 */
@RestController
@RequestMapping("/lead-pool")
@Tag(name = "线索池")
public class CluePoolController {

    /** 线索池服务 */
    @Resource
    private CluePoolService cluePoolService;

    /** 数据权限服务 */
    @Resource
    private DataScopeService dataScopeService;

    /**
     * 线索池分页列表
     *
     * @param request 分页请求
     * @return 分页响应
     */
    @PostMapping("/page")
    @RequiresPermissions(PermissionConstants.CLUE_POOL_READ)
    @Operation(summary = "线索池列表")
    public PagerWithOption<List<ClueListResponse>> list(@Validated @RequestBody CluePageRequest request) {
        DeptDataPermissionDTO deptDataPermission = dataScopeService.getDeptDataPermission(
                SessionUtils.getUserId(), OrganizationContext.getOrganizationId(),
                request.getViewId(), PermissionConstants.CLUE_POOL_READ);
        return cluePoolService.list(request, OrganizationContext.getOrganizationId(), deptDataPermission);
    }

    /**
     * 从线索池领取线索
     *
     * @param request 领取请求
     */
    @PostMapping("/claim")
    @RequiresPermissions(PermissionConstants.CLUE_POOL_CLAIM)
    @Operation(summary = "领取线索")
    public void claim(@Validated @RequestBody CluePoolClaimRequest request) {
        cluePoolService.claim(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 从线索池分配线索
     *
     * @param request 分配请求
     */
    @PostMapping("/assign")
    @RequiresPermissions(PermissionConstants.CLUE_POOL_ASSIGN)
    @Operation(summary = "分配线索")
    public void assign(@Validated @RequestBody CluePoolAssignRequest request) {
        cluePoolService.assign(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }
}
