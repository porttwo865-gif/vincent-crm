package cn.vincent.crm.contract.plan.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 更新回款计划请求
 */
@Data
@Schema(description = "更新回款计划请求")
public class PaymentPlanUpdateRequest {

    /** 回款计划 ID */
    @NotBlank(message = "回款计划ID不能为空")
    @Schema(description = "回款计划ID")
    private String id;

    /** 期数 */
    @Schema(description = "期数")
    private Integer planNum;

    /** 计划金额 */
    @Schema(description = "计划金额")
    private BigDecimal amount;

    /** 预计回款日期 */
    @Schema(description = "预计回款日期")
    private Long expectedDate;

    /** 实际回款日期 */
    @Schema(description = "实际回款日期")
    private Long actualDate;

    /** 状态 */
    @Schema(description = "状态")
    private String status;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
