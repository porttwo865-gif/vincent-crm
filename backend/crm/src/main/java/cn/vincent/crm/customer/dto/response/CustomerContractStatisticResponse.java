package cn.vincent.crm.customer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 客户合同统计响应 DTO
 */
@Data
@Schema(description = "客户合同统计响应")
public class CustomerContractStatisticResponse {

    /** 合同总金额 */
    @Schema(description = "合同总金额")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /** 已回款金额 */
    @Schema(description = "已回款金额")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    /** 未回款金额 */
    @Schema(description = "未回款金额")
    private BigDecimal unpaidAmount = BigDecimal.ZERO;

    /** 已开票金额 */
    @Schema(description = "已开票金额")
    private BigDecimal invoicedAmount = BigDecimal.ZERO;

    /** 未开票金额 */
    @Schema(description = "未开票金额")
    private BigDecimal uninvoicedAmount = BigDecimal.ZERO;
}
