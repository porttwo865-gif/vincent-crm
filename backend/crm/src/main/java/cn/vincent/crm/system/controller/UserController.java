package cn.vincent.crm.system.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.system.domain.User;
import cn.vincent.crm.system.dto.request.ResetPasswordRequest;
import cn.vincent.crm.system.dto.request.UserAddRequest;
import cn.vincent.crm.system.dto.request.UserPageRequest;
import cn.vincent.crm.system.dto.request.UserUpdateRequest;
import cn.vincent.crm.system.dto.response.UserListResponse;
import cn.vincent.crm.system.service.UserService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
public class UserController {

    /** 用户服务 */
    @Resource
    private UserService userService;

    /**
     * 新增用户
     *
     * @param request 新增请求
     * @return 新增的用户实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.SYSTEM_USER_ADD)
    @Operation(summary = "新增用户")
    public User add(@Validated @RequestBody UserAddRequest request) {
        return userService.add(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 更新用户
     *
     * @param request 更新请求
     * @return 更新后的用户实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.SYSTEM_USER_UPDATE)
    @Operation(summary = "更新用户")
    public User update(@Validated @RequestBody UserUpdateRequest request) {
        return userService.update(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 删除用户
     *
     * @param id 用户 ID
     */
    @GetMapping("/delete/{id}")
    @RequiresPermissions(PermissionConstants.SYSTEM_USER_DELETE)
    @Operation(summary = "删除用户")
    public void delete(@PathVariable String id) {
        userService.delete(id);
    }

    /**
     * 用户列表（分页）
     *
     * @param request 分页请求
     * @return 分页结果
     */
    @PostMapping("/page")
    @RequiresPermissions(PermissionConstants.SYSTEM_USER_READ)
    @Operation(summary = "用户列表")
    public PagerWithOption<List<UserListResponse>> page(@Validated @RequestBody UserPageRequest request) {
        return userService.page(request, OrganizationContext.getOrganizationId());
    }

    /**
     * 启用用户
     *
     * @param id 用户 ID
     */
    @GetMapping("/enable/{id}")
    @RequiresPermissions(PermissionConstants.SYSTEM_USER_UPDATE)
    @Operation(summary = "启用用户")
    public void enable(@PathVariable String id) {
        userService.enable(id);
    }

    /**
     * 禁用用户
     *
     * @param id 用户 ID
     */
    @GetMapping("/disable/{id}")
    @RequiresPermissions(PermissionConstants.SYSTEM_USER_UPDATE)
    @Operation(summary = "禁用用户")
    public void disable(@PathVariable String id) {
        userService.disable(id);
    }

    /**
     * 重置密码
     *
     * @param request 重置密码请求
     */
    @PostMapping("/reset-password")
    @RequiresPermissions(PermissionConstants.SYSTEM_USER_UPDATE)
    @Operation(summary = "重置密码")
    public void resetPassword(@Validated @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request.getId(), request.getNewPassword());
    }
}
