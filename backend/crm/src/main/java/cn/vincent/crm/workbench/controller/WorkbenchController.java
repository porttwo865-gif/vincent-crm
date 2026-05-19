package cn.vincent.crm.workbench.controller;

import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.workbench.dto.response.WorkbenchOverviewResponse;
import cn.vincent.crm.workbench.dto.response.WorkbenchRecentResponse;
import cn.vincent.crm.workbench.dto.response.WorkbenchTodoResponse;
import cn.vincent.crm.workbench.service.WorkbenchService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
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
     * 业绩概览
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
     * 待办事项
     *
     * @return 待办事项
     */
    @GetMapping("/todo")
    @Operation(summary = "待办事项")
    public WorkbenchTodoResponse todo() {
        return workbenchService.todo(
                SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 最近动态
     *
     * @return 最近动态列表
     */
    @GetMapping("/recent")
    @Operation(summary = "最近动态")
    public List<WorkbenchRecentResponse> recent() {
        return workbenchService.recent(
                SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }
}
