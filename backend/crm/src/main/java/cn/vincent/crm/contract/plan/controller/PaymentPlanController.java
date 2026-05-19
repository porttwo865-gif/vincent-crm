package cn.vincent.crm.contract.plan.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.crm.contract.plan.domain.PaymentPlan;
import cn.vincent.crm.contract.plan.dto.request.PaymentPlanAddRequest;
import cn.vincent.crm.contract.plan.dto.request.PaymentPlanUpdateRequest;
import cn.vincent.crm.contract.plan.dto.response.PaymentPlanListResponse;
import cn.vincent.crm.contract.plan.service.PaymentPlanService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 回款计划控制器
 */
@RestController
@RequestMapping("/contract/payment-plan")
@Tag(name = "回款计划管理")
public class PaymentPlanController {

    @Resource
    private PaymentPlanService paymentPlanService;

    /**
     * 新增回款计划
     *
     * @param request 新增请求
     * @return 新增的回款计划实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.CONTRACT_MANAGEMENT_ADD)
    @Operation(summary = "新增回款计划")
    public PaymentPlan add(@Validated @RequestBody PaymentPlanAddRequest request) {
        return paymentPlanService.add(request, SessionUtils.getUserId());
    }

    /**
     * 更新回款计划
     *
     * @param request 更新请求
     * @return 更新后的回款计划实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.CONTRACT_MANAGEMENT_UPDATE)
    @Operation(summary = "更新回款计划")
    public PaymentPlan update(@Validated @RequestBody PaymentPlanUpdateRequest request) {
        return paymentPlanService.update(request, SessionUtils.getUserId());
    }

    /**
     * 删除回款计划
     *
     * @param id 回款计划 ID
     */
    @GetMapping("/delete/{id}")
    @RequiresPermissions(PermissionConstants.CONTRACT_MANAGEMENT_DELETE)
    @Operation(summary = "删除回款计划")
    public void delete(@PathVariable String id) {
        paymentPlanService.delete(id);
    }

    /**
     * 根据合同 ID 查询回款计划列表
     *
     * @param contractId 合同 ID
     * @return 回款计划列表
     */
    @GetMapping("/list/{contractId}")
    @RequiresPermissions(PermissionConstants.CONTRACT_MANAGEMENT_READ)
    @Operation(summary = "回款计划列表")
    public List<PaymentPlanListResponse> listByContractId(@PathVariable String contractId) {
        return paymentPlanService.listByContractId(contractId);
    }
}
