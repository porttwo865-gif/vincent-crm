package cn.vincent.crm.contract.record.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.crm.contract.record.domain.PaymentRecord;
import cn.vincent.crm.contract.record.dto.request.PaymentRecordAddRequest;
import cn.vincent.crm.contract.record.dto.request.PaymentRecordUpdateRequest;
import cn.vincent.crm.contract.record.dto.response.PaymentRecordListResponse;
import cn.vincent.crm.contract.record.service.PaymentRecordService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 回款记录控制器
 */
@RestController
@RequestMapping("/contract/payment-record")
@Tag(name = "回款记录管理")
public class PaymentRecordController {

    @Resource
    private PaymentRecordService paymentRecordService;

    /**
     * 新增回款记录
     *
     * @param request 新增请求
     * @return 新增的回款记录实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.CONTRACT_MANAGEMENT_ADD)
    @Operation(summary = "新增回款记录")
    public PaymentRecord add(@Validated @RequestBody PaymentRecordAddRequest request) {
        return paymentRecordService.add(request, SessionUtils.getUserId());
    }

    /**
     * 更新回款记录
     *
     * @param request 更新请求
     * @return 更新后的回款记录实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.CONTRACT_MANAGEMENT_UPDATE)
    @Operation(summary = "更新回款记录")
    public PaymentRecord update(@Validated @RequestBody PaymentRecordUpdateRequest request) {
        return paymentRecordService.update(request, SessionUtils.getUserId());
    }

    /**
     * 删除回款记录
     *
     * @param id 回款记录 ID
     */
    @GetMapping("/delete/{id}")
    @RequiresPermissions(PermissionConstants.CONTRACT_MANAGEMENT_DELETE)
    @Operation(summary = "删除回款记录")
    public void delete(@PathVariable String id) {
        paymentRecordService.delete(id);
    }

    /**
     * 根据合同 ID 查询回款记录列表
     *
     * @param contractId 合同 ID
     * @return 回款记录列表
     */
    @GetMapping("/list/{contractId}")
    @RequiresPermissions(PermissionConstants.CONTRACT_MANAGEMENT_READ)
    @Operation(summary = "回款记录列表")
    public List<PaymentRecordListResponse> listByContractId(@PathVariable String contractId) {
        return paymentRecordService.listByContractId(contractId);
    }
}
