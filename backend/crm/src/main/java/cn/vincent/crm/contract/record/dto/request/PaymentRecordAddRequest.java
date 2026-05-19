package cn.vincent.crm.contract.record.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 新增回款记录请求
 */
@Data
@Schema(description = "新增回款记录请求")
public class PaymentRecordAddRequest {

    /** 关联合同 ID */
    @NotBlank(message = "合同ID不能为空")
    @Schema(description = "关联合同ID")
    private String contractId;

    /** 关联回款计划 ID */
    @Schema(description = "关联回款计划ID")
    private String planId;

    /** 回款金额 */
    @Schema(description = "回款金额")
    private BigDecimal amount;

    /** 回款日期 */
    @Schema(description = "回款日期")
    private Long paymentDate;

    /** 回款方式 */
    @Schema(description = "回款方式")
    private String paymentMethod;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
