package cn.vincent.crm.follow.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.follow.domain.FollowPlan;
import cn.vincent.crm.follow.dto.request.FollowPlanAddRequest;
import cn.vincent.crm.follow.dto.request.FollowPlanListRequest;
import cn.vincent.crm.follow.dto.request.FollowPlanUpdateRequest;
import cn.vincent.crm.follow.dto.response.FollowPlanListResponse;
import cn.vincent.crm.follow.service.FollowPlanService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 跟进计划控制器
 */
@RestController
@RequestMapping("/follow/plan")
@Tag(name = "跟进计划管理")
public class FollowPlanController {

    /** 跟进计划服务 */
    @Resource
    private FollowPlanService followPlanService;

    /**
     * 添加跟进计划
     *
     * @param request 添加请求
     * @return 新增的跟进计划实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.FOLLOW_PLAN_ADD)
    @Operation(summary = "添加跟进计划")
    public FollowPlan add(@Validated @RequestBody FollowPlanAddRequest request) {
        return followPlanService.add(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 更新跟进计划
     *
     * @param request 更新请求
     * @return 更新后的跟进计划实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.FOLLOW_PLAN_UPDATE)
    @Operation(summary = "更新跟进计划")
    public FollowPlan update(@Validated @RequestBody FollowPlanUpdateRequest request) {
        return followPlanService.update(request, SessionUtils.getUserId());
    }

    /**
     * 删除跟进计划
     *
     * @param id 跟进计划 ID
     */
    @PostMapping("/delete")
    @RequiresPermissions(PermissionConstants.FOLLOW_PLAN_DELETE)
    @Operation(summary = "删除跟进计划")
    public void delete(@RequestBody String id) {
        followPlanService.delete(id);
    }

    /**
     * 标记跟进计划为完成
     *
     * @param id 跟进计划 ID
     */
    @PostMapping("/done")
    @RequiresPermissions(PermissionConstants.FOLLOW_PLAN_UPDATE)
    @Operation(summary = "标记跟进计划完成")
    public void done(@RequestBody String id) {
        followPlanService.done(id, SessionUtils.getUserId());
    }

    /**
     * 查询跟进计划列表
     *
     * @param request 列表查询请求
     * @return 跟进计划列表
     */
    @PostMapping("/list")
    @RequiresPermissions(PermissionConstants.FOLLOW_PLAN_READ)
    @Operation(summary = "跟进计划列表")
    public List<FollowPlanListResponse> list(@Validated @RequestBody FollowPlanListRequest request) {
        return followPlanService.list(request.getBizType(), request.getBizId());
    }

    /**
     * 查询我的待办跟进计划（用于工作台）
     *
     * @return 待办跟进计划列表
     */
    @PostMapping("/my")
    @RequiresPermissions(PermissionConstants.FOLLOW_PLAN_READ)
    @Operation(summary = "我的待办跟进计划")
    public List<FollowPlanListResponse> myPending() {
        return followPlanService.myPending(SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }
}
