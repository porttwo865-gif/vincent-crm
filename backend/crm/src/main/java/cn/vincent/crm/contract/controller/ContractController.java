package cn.vincent.crm.contract.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.FormKey;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.contract.domain.Contract;
import cn.vincent.crm.contract.dto.request.ContractAddRequest;
import cn.vincent.crm.contract.dto.request.ContractPageRequest;
import cn.vincent.crm.contract.dto.request.ContractStatusRequest;
import cn.vincent.crm.contract.dto.request.ContractUpdateRequest;
import cn.vincent.crm.contract.dto.response.ContractGetResponse;
import cn.vincent.crm.contract.dto.response.ContractListResponse;
import cn.vincent.crm.contract.service.ContractService;
import cn.vincent.crm.system.dto.response.ModuleFormConfigDTO;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.security.DataScopeService;
import cn.vincent.security.SessionUtils;
import cn.vincent.crm.system.service.ModuleFormCacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 合同控制器
 */
@RestController
@RequestMapping("/contract")
@Tag(name = "合同管理")
public class ContractController {

    @Resource
    private ContractService contractService;

    @Resource
    private ModuleFormCacheService moduleFormCacheService;

    @Resource
    private DataScopeService dataScopeService;

    /**
     * 获取合同表单配置
     *
     * @return 表单配置
     */
    @GetMapping("/module/form")
    @RequiresPermissions(PermissionConstants.CONTRACT_MANAGEMENT_READ)
    @Operation(summary = "获取合同表单配置")
    public ModuleFormConfigDTO getModuleFormConfig() {
        return moduleFormCacheService.getBusinessFormConfig(
                FormKey.CONTRACT.getKey(), OrganizationContext.getOrganizationId());
    }

    /**
     * 合同列表
     *
     * @param request 分页请求
     * @return 分页结果
     */
    @PostMapping("/page")
    @RequiresPermissions(PermissionConstants.CONTRACT_MANAGEMENT_READ)
    @Operation(summary = "合同列表")
    public PagerWithOption<List<ContractListResponse>> list(@Validated @RequestBody ContractPageRequest request) {
        return contractService.list(request, SessionUtils.getUserId(),
                OrganizationContext.getOrganizationId(), null);
    }

    /**
     * 新增合同
     *
     * @param request 新增请求
     * @return 新增的合同实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.CONTRACT_MANAGEMENT_ADD)
    @Operation(summary = "新增合同")
    public Contract add(@Validated @RequestBody ContractAddRequest request) {
        return contractService.add(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 更新合同
     *
     * @param request 更新请求
     * @return 更新后的合同实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.CONTRACT_MANAGEMENT_UPDATE)
    @Operation(summary = "更新合同")
    public Contract update(@Validated @RequestBody ContractUpdateRequest request) {
        return contractService.update(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 删除合同
     *
     * @param id 合同 ID
     */
    @GetMapping("/delete/{id}")
    @RequiresPermissions(PermissionConstants.CONTRACT_MANAGEMENT_DELETE)
    @Operation(summary = "删除合同")
    public void delete(@PathVariable String id) {
        contractService.delete(id);
    }

    /**
     * 合同详情
     *
     * @param id 合同 ID
     * @return 合同详情
     */
    @GetMapping("/get/{id}")
    @RequiresPermissions(PermissionConstants.CONTRACT_MANAGEMENT_READ)
    @Operation(summary = "合同详情")
    public ContractGetResponse get(@PathVariable String id) {
        return contractService.getWithDataPermissionCheck(
                id, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 变更合同状态
     *
     * @param request 状态变更请求
     * @return 更新后的合同实体
     */
    @PostMapping("/status")
    @RequiresPermissions(PermissionConstants.CONTRACT_MANAGEMENT_UPDATE)
    @Operation(summary = "变更合同状态")
    public Contract changeStatus(@Validated @RequestBody ContractStatusRequest request) {
        return contractService.changeStatus(request, SessionUtils.getUserId());
    }
}
