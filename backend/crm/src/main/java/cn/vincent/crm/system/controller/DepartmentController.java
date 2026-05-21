package cn.vincent.crm.system.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.system.domain.Department;
import cn.vincent.crm.system.dto.request.DepartmentAddRequest;
import cn.vincent.crm.system.dto.request.DepartmentUpdateRequest;
import cn.vincent.crm.system.dto.response.DepartmentTreeResponse;
import cn.vincent.crm.system.service.DepartmentService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 */
@RestController
@RequestMapping("/system/department")
@Tag(name = "部门管理")
public class DepartmentController {

    /** 部门服务 */
    @Resource
    private DepartmentService departmentService;

    /**
     * 新增部门
     *
     * @param request 新增请求
     * @return 新增的部门实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.SYSTEM_DEPARTMENT_ADD)
    @Operation(summary = "新增部门")
    public Department add(@Validated @RequestBody DepartmentAddRequest request) {
        return departmentService.add(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 更新部门
     *
     * @param request 更新请求
     * @return 更新后的部门实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.SYSTEM_DEPARTMENT_UPDATE)
    @Operation(summary = "更新部门")
    public Department update(@Validated @RequestBody DepartmentUpdateRequest request) {
        return departmentService.update(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 删除部门
     *
     * @param id 部门 ID
     */
    @GetMapping("/delete/{id}")
    @RequiresPermissions(PermissionConstants.SYSTEM_DEPARTMENT_DELETE)
    @Operation(summary = "删除部门")
    public void delete(@PathVariable String id) {
        departmentService.delete(id);
    }

    /**
     * 部门树
     *
     * @return 部门树形列表
     */
    @GetMapping("/tree")
    @RequiresPermissions(PermissionConstants.SYSTEM_DEPARTMENT_READ)
    @Operation(summary = "部门树")
    public List<DepartmentTreeResponse> tree() {
        return departmentService.tree(OrganizationContext.getOrganizationId());
    }
}
