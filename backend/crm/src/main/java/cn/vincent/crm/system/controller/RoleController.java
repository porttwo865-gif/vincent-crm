package cn.vincent.crm.system.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.system.domain.Role;
import cn.vincent.crm.system.dto.request.RoleAddRequest;
import cn.vincent.crm.system.dto.request.RoleUpdateRequest;
import cn.vincent.crm.system.dto.response.RoleListResponse;
import cn.vincent.crm.system.service.RoleService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/system/role")
@Tag(name = "角色管理")
public class RoleController {

    /** 角色服务 */
    @Resource
    private RoleService roleService;

    /**
     * 新增角色
     *
     * @param request 新增请求
     * @return 新增的角色实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.SYSTEM_ROLE_ADD)
    @Operation(summary = "新增角色")
    public Role add(@Validated @RequestBody RoleAddRequest request) {
        return roleService.add(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 更新角色
     *
     * @param request 更新请求
     * @return 更新后的角色实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.SYSTEM_ROLE_UPDATE)
    @Operation(summary = "更新角色")
    public Role update(@Validated @RequestBody RoleUpdateRequest request) {
        return roleService.update(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 删除角色
     *
     * @param id 角色 ID
     */
    @GetMapping("/delete/{id}")
    @RequiresPermissions(PermissionConstants.SYSTEM_ROLE_DELETE)
    @Operation(summary = "删除角色")
    public void delete(@PathVariable String id) {
        roleService.delete(id);
    }

    /**
     * 角色列表
     *
     * @return 角色列表
     */
    @GetMapping("/list")
    @RequiresPermissions(PermissionConstants.SYSTEM_ROLE_READ)
    @Operation(summary = "角色列表")
    public List<RoleListResponse> list() {
        return roleService.list(OrganizationContext.getOrganizationId());
    }

    /**
     * 角色权限
     *
     * @param id 角色 ID
     * @return 权限 ID 列表
     */
    @GetMapping("/permissions/{id}")
    @RequiresPermissions(PermissionConstants.SYSTEM_ROLE_READ)
    @Operation(summary = "角色权限")
    public List<String> getPermissions(@PathVariable String id) {
        return roleService.getPermissions(id);
    }
}
