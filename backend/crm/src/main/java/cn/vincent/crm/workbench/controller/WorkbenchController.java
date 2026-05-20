package cn.vincent.crm.workbench.controller;

import cn.vincent.common.response.PagerWithOption;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.workbench.dto.request.WorkbenchActivityPageRequest;
import cn.vincent.crm.workbench.dto.response.WorkbenchActivityItemResponse;
import cn.vincent.crm.workbench.dto.response.WorkbenchOverviewResponse;
import cn.vincent.crm.workbench.dto.response.WorkbenchRecentResponse;
import cn.vincent.crm.workbench.dto.response.WorkbenchTodoItemResponse;
import cn.vincent.crm.workbench.service.WorkbenchService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 工作台控制器
 */
@RestController
@RequestMapping("/workbench")
@Tag(name = "工作台")
public class WorkbenchController {

    /** 工作台服务 */
    @Resource
    private WorkbenchService workbenchService;

    /**
     * 业绩概览（与 /stats 同源，保留兼容）
     *
     * @return 业绩概览
     */
    @GetMapping("/overview")
    @Operation(summary = "业绩概览")
    public WorkbenchOverviewResponse overview() {
        return workbenchService.overview(
                SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 工作台业绩统计
     *
     * @return 业绩统计
     */
    @GetMapping("/stats")
    @Operation(summary = "工作台业绩统计（与/overview同源）")
    public WorkbenchOverviewResponse stats() {
        return workbenchService.overview(
                SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 待办事项（条目列表格式）
     *
     * @return 待办事项列表
     */
    @GetMapping("/todo")
    @Operation(summary = "待办事项")
    public List<WorkbenchTodoItemResponse> todo() {
        return workbenchService.getTodoItems(
                SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 最近动态（非分页，保留兼容）
     *
     * @return 最近动态列表
     */
    @GetMapping("/recent")
    @Operation(summary = "最近动态")
    public List<WorkbenchRecentResponse> recent() {
        return workbenchService.recent(
                SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 最近动态（分页）
     *
     * @param request 分页请求
     * @return 分页动态列表
     */
    @PostMapping("/activity")
    @Operation(summary = "工作台最近动态（分页）")
    public PagerWithOption<List<WorkbenchActivityItemResponse>> listActivity(
            @Validated @RequestBody WorkbenchActivityPageRequest request) {
        return workbenchService.listActivity(request,
                SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }
}
