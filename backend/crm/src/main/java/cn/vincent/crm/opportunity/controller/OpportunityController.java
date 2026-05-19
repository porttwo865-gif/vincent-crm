package cn.vincent.crm.opportunity.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.FormKey;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.opportunity.domain.Opportunity;
import cn.vincent.crm.opportunity.dto.request.OpportunityAddRequest;
import cn.vincent.crm.opportunity.dto.request.OpportunityPageRequest;
import cn.vincent.crm.opportunity.dto.request.OpportunityPosRequest;
import cn.vincent.crm.opportunity.dto.request.OpportunityStageRequest;
import cn.vincent.crm.opportunity.dto.request.OpportunityUpdateRequest;
import cn.vincent.crm.opportunity.dto.response.OpportunityGetResponse;
import cn.vincent.crm.opportunity.dto.response.OpportunityListResponse;
import cn.vincent.crm.opportunity.service.OpportunityService;
import cn.vincent.crm.system.dto.response.ModuleFormConfigDTO;
import cn.vincent.crm.system.service.ModuleFormCacheService;
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
 * 商机管理控制器
 */
@RestController
@RequestMapping("/opportunity")
@Tag(name = "商机管理")
public class OpportunityController {

    /** 商机服务 */
    @Resource
    private OpportunityService opportunityService;

    /** 数据权限服务 */
    @Resource
    private DataScopeService dataScopeService;

    /** 模块表单缓存服务 */
    @Resource
    private ModuleFormCacheService moduleFormCacheService;

    /**
     * 获取商机表单配置
     *
     * @return 表单配置
     */
    @GetMapping("/module/form")
    @RequiresPermissions(PermissionConstants.OPPORTUNITY_MANAGEMENT_READ)
    @Operation(summary = "获取商机表单配置")
    public ModuleFormConfigDTO getModuleFormConfig() {
        return moduleFormCacheService.getBusinessFormConfig(
                FormKey.OPPORTUNITY.getKey(), OrganizationContext.getOrganizationId());
    }

    /**
     * 商机分页列表
     *
     * @param request 分页请求
     * @return 分页响应
     */
    @PostMapping("/page")
    @RequiresPermissions(PermissionConstants.OPPORTUNITY_MANAGEMENT_READ)
    @Operation(summary = "商机列表")
    public PagerWithOption<List<OpportunityListResponse>> list(@Validated @RequestBody OpportunityPageRequest request) {
        DeptDataPermissionDTO deptDataPermission = dataScopeService.getDeptDataPermission(
                SessionUtils.getUserId(), OrganizationContext.getOrganizationId(),
                request.getViewId(), PermissionConstants.OPPORTUNITY_MANAGEMENT_READ);
        return opportunityService.list(request, SessionUtils.getUserId(),
                OrganizationContext.getOrganizationId(), deptDataPermission);
    }

    /**
     * 新增商机
     *
     * @param request 新增请求
     * @return 新增的商机实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.OPPORTUNITY_MANAGEMENT_ADD)
    @Operation(summary = "新增商机")
    public Opportunity add(@Validated @RequestBody OpportunityAddRequest request) {
        return opportunityService.add(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 更新商机
     *
     * @param request 更新请求
     * @return 更新后的商机实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.OPPORTUNITY_MANAGEMENT_UPDATE)
    @Operation(summary = "更新商机")
    public Opportunity update(@Validated @RequestBody OpportunityUpdateRequest request) {
        return opportunityService.update(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 删除商机
     *
     * @param id 商机 ID
     */
    @GetMapping("/delete/{id}")
    @RequiresPermissions(PermissionConstants.OPPORTUNITY_MANAGEMENT_DELETE)
    @Operation(summary = "删除商机")
    public void delete(@PathVariable String id) {
        opportunityService.delete(id);
    }

    /**
     * 批量删除商机
     *
     * @param ids 商机 ID 列表
     */
    @PostMapping("/batch/delete")
    @RequiresPermissions(PermissionConstants.OPPORTUNITY_MANAGEMENT_DELETE)
    @Operation(summary = "批量删除商机")
    public void batchDelete(@RequestBody List<String> ids) {
        opportunityService.batchDelete(ids);
    }

    /**
     * 商机详情
     *
     * @param id 商机 ID
     * @return 商机详情响应
     */
    @GetMapping("/get/{id}")
    @RequiresPermissions(PermissionConstants.OPPORTUNITY_MANAGEMENT_READ)
    @Operation(summary = "商机详情")
    public OpportunityGetResponse get(@PathVariable String id) {
        return opportunityService.getWithDataPermissionCheck(
                id, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 更新商机阶段
     *
     * @param request 阶段更新请求
     */
    @PostMapping("/stage")
    @RequiresPermissions(PermissionConstants.OPPORTUNITY_MANAGEMENT_UPDATE)
    @Operation(summary = "更新商机阶段")
    public void updateStage(@Validated @RequestBody OpportunityStageRequest request) {
        opportunityService.updateStage(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 看板拖拽排序
     *
     * @param request 排序请求
     */
    @PostMapping("/edit/pos")
    @RequiresPermissions(PermissionConstants.OPPORTUNITY_MANAGEMENT_UPDATE)
    @Operation(summary = "看板拖拽排序")
    public void updatePos(@Validated @RequestBody OpportunityPosRequest request) {
        opportunityService.updatePos(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }
}
