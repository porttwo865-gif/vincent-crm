package cn.vincent.crm.system.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.system.domain.ModuleField;
import cn.vincent.crm.system.dto.request.ModuleFieldAddRequest;
import cn.vincent.crm.system.dto.request.ModuleFieldUpdateRequest;
import cn.vincent.crm.system.dto.response.ModuleFieldDTO;
import cn.vincent.crm.system.service.ModuleFieldService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模块字段管理控制器
 */
@RestController
@RequestMapping("/system/module-field")
@Tag(name = "模块字段管理")
public class ModuleFieldController {

    /** 模块字段服务 */
    @Resource
    private ModuleFieldService moduleFieldService;

    /**
     * 添加字段
     *
     * @param request 添加请求
     * @return 新增的字段实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.MODULE_FORM_UPDATE)
    @Operation(summary = "添加字段")
    public ModuleField add(@Validated @RequestBody ModuleFieldAddRequest request) {
        return moduleFieldService.addField(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 更新字段
     *
     * @param request 更新请求
     * @return 更新后的字段实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.MODULE_FORM_UPDATE)
    @Operation(summary = "更新字段")
    public ModuleField update(@Validated @RequestBody ModuleFieldUpdateRequest request) {
        return moduleFieldService.updateField(request, SessionUtils.getUserId());
    }

    /**
     * 删除字段
     *
     * @param id 字段 ID
     */
    @GetMapping("/delete/{id}")
    @RequiresPermissions(PermissionConstants.MODULE_FORM_UPDATE)
    @Operation(summary = "删除字段")
    public void delete(@PathVariable String id) {
        moduleFieldService.deleteField(id);
    }

    /**
     * 字段列表
     *
     * @param formKey 表单标识
     * @return 字段 DTO 列表
     */
    @GetMapping("/list/{formKey}")
    @RequiresPermissions(PermissionConstants.MODULE_FORM_READ)
    @Operation(summary = "字段列表")
    public List<ModuleFieldDTO> list(@PathVariable String formKey) {
        return moduleFieldService.getFieldList(formKey, OrganizationContext.getOrganizationId());
    }

    /**
     * 字段排序
     *
     * @param fieldIds 排序后的字段 ID 列表
     */
    @PostMapping("/sort")
    @RequiresPermissions(PermissionConstants.MODULE_FORM_UPDATE)
    @Operation(summary = "字段排序")
    public void sort(@RequestBody List<String> fieldIds) {
        moduleFieldService.sortFields(fieldIds);
    }
}
