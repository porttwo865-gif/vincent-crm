package cn.vincent.crm.workbench.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 工作台业绩概览响应 DTO
 */
@Data
@Schema(description = "工作台业绩概览响应")
public class WorkbenchOverviewResponse {

    /** 我的线索总数 */
    @Schema(description = "我的线索总数")
    private Long clueCount;

    /** 我的客户总数 */
    @Schema(description = "我的客户总数")
    private Long customerCount;

    /** 我的商机总数 */
    @Schema(description = "我的商机总数")
    private Long opportunityCount;

    /** 我的商机总金额 */
    @Schema(description = "我的商机总金额")
    private BigDecimal opportunityAmount;

    /** 本月新签合同数 */
    @Schema(description = "本月新签合同数")
    private Long contractCount;

    /** 本月合同总金额 */
    @Schema(description = "本月合同总金额")
    private BigDecimal contractAmount;

    /** 本月订单数 */
    @Schema(description = "本月订单数")
    private Long orderCount;

    /** 本月订单总金额 */
    @Schema(description = "本月订单总金额")
    private BigDecimal orderAmount;
}
