package cn.vincent.crm.contract.record.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 回款记录列表响应
 */
@Data
@Schema(description = "回款记录列表响应")
public class PaymentRecordListResponse {

    /** 回款记录 ID */
    @Schema(description = "回款记录ID")
    private String id;

    /** 关联合同 ID */
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

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Long createTime;

    /** 创建人姓名 */
    @Schema(description = "创建人姓名")
    private String createUserName;
}
