package cn.vincent.crm.clue.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.clue.domain.Clue;
import cn.vincent.crm.clue.dto.request.ClueAddRequest;
import cn.vincent.crm.clue.dto.request.ClueMovePoolRequest;
import cn.vincent.crm.clue.dto.request.CluePageRequest;
import cn.vincent.crm.clue.dto.request.ClueTransformRequest;
import cn.vincent.crm.clue.dto.request.ClueUpdateRequest;
import cn.vincent.crm.clue.dto.response.ClueGetResponse;
import cn.vincent.crm.clue.dto.response.ClueListResponse;
import cn.vincent.crm.clue.service.ClueService;
import cn.vincent.crm.system.dto.response.ModuleFormConfigDTO;
import cn.vincent.crm.system.service.ModuleFormCacheService;
import cn.vincent.common.constants.FormKey;
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
 * 线索管理控制器
 */
@RestController
@RequestMapping("/clue")
@Tag(name = "线索管理")
public class ClueController {

    /** 线索服务 */
    @Resource
    private ClueService clueService;

    /** 数据权限服务 */
    @Resource
    private DataScopeService dataScopeService;

    /** 表单缓存服务 */
    @Resource
    private ModuleFormCacheService moduleFormCacheService;

    /**
     * 获取线索表单配置
     *
     * @return 表单配置
     */
    @GetMapping("/module/form")
    @RequiresPermissions(PermissionConstants.CLUE_MANAGEMENT_READ)
    @Operation(summary = "获取线索表单配置")
    public ModuleFormConfigDTO getModuleFormConfig() {
        return moduleFormCacheService.getBusinessFormConfig(
                FormKey.CLUE.getKey(), OrganizationContext.getOrganizationId());
    }

    /**
     * 线索分页列表
     *
     * @param request 分页请求
     * @return 分页响应
     */
    @PostMapping("/page")
    @RequiresPermissions(PermissionConstants.CLUE_MANAGEMENT_READ)
    @Operation(summary = "线索列表")
    public PagerWithOption<List<ClueListResponse>> list(@Validated @RequestBody CluePageRequest request) {
        DeptDataPermissionDTO deptDataPermission = dataScopeService.getDeptDataPermission(
                SessionUtils.getUserId(), OrganizationContext.getOrganizationId(),
                request.getViewId(), PermissionConstants.CLUE_MANAGEMENT_READ);
        return clueService.list(request, SessionUtils.getUserId(),
                OrganizationContext.getOrganizationId(), deptDataPermission);
    }

    /**
     * 新增线索
     *
     * @param request 新增请求
     * @return 新增的线索实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.CLUE_MANAGEMENT_ADD)
    @Operation(summary = "新增线索")
    public Clue add(@Validated @RequestBody ClueAddRequest request) {
        return clueService.add(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 更新线索
     *
     * @param request 更新请求
     * @return 更新后的线索实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.CLUE_MANAGEMENT_UPDATE)
    @Operation(summary = "更新线索")
    public Clue update(@Validated @RequestBody ClueUpdateRequest request) {
        return clueService.update(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 删除线索
     *
     * @param id 线索 ID
     */
    @GetMapping("/delete/{id}")
    @RequiresPermissions(PermissionConstants.CLUE_MANAGEMENT_DELETE)
    @Operation(summary = "删除线索")
    public void delete(@PathVariable String id) {
        clueService.delete(id);
    }

    /**
     * 批量删除线索
     *
     * @param ids 线索 ID 列表
     */
    @PostMapping("/batch/delete")
    @RequiresPermissions(PermissionConstants.CLUE_MANAGEMENT_DELETE)
    @Operation(summary = "批量删除线索")
    public void batchDelete(@RequestBody List<String> ids) {
        clueService.batchDelete(ids);
    }

    /**
     * 线索详情
     *
     * @param id 线索 ID
     * @return 线索详情响应
     */
    @GetMapping("/get/{id}")
    @RequiresPermissions(PermissionConstants.CLUE_MANAGEMENT_READ)
    @Operation(summary = "线索详情")
    public ClueGetResponse get(@PathVariable String id) {
        return clueService.getWithDataPermissionCheck(id, SessionUtils.getUserId(),
                OrganizationContext.getOrganizationId());
    }

    /**
     * 线索转客户
     *
     * @param request 转化请求
     * @return 更新后的线索实体
     */
    @PostMapping("/transform")
    @RequiresPermissions(PermissionConstants.CLUE_MANAGEMENT_TRANSFORM)
    @Operation(summary = "线索转客户")
    public Clue transform(@Validated @RequestBody ClueTransformRequest request) {
        return clueService.transform(request, SessionUtils.getUserId(),
                OrganizationContext.getOrganizationId());
    }

    /**
     * 移入线索池
     *
     * @param request 移入线索池请求
     */
    @PostMapping("/move-pool")
    @RequiresPermissions(PermissionConstants.CLUE_MANAGEMENT_RECYCLE)
    @Operation(summary = "移入线索池")
    public void movePool(@Validated @RequestBody ClueMovePoolRequest request) {
        clueService.moveToPool(request, SessionUtils.getUserId(),
                OrganizationContext.getOrganizationId());
    }
}
