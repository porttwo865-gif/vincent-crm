package cn.vincent.crm.workbench.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 全局搜索订单结果项
 */
@Data
@Schema(description = "全局搜索订单结果项")
public class GlobalSearchOrderItem {

    /** 订单 ID */
    @Schema(description = "订单ID")
    private String id;

    /** 订单编号 */
    @Schema(description = "订单编号")
    private String orderNo;

    /** 关联客户名称 */
    @Schema(description = "关联客户名称")
    private String customerName;

    /** 订单总金额 */
    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    /** 状态 */
    @Schema(description = "状态")
    private String status;
}
