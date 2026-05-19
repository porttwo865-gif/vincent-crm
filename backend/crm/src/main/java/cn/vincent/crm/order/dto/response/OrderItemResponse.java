package cn.vincent.crm.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单明细响应
 */
@Data
@Schema(description = "订单明细响应")
public class OrderItemResponse {

    /** 明细 ID */
    @Schema(description = "明细ID")
    private String id;

    /** 关联订单 ID */
    @Schema(description = "关联订单ID")
    private String orderId;

    /** 产品 ID */
    @Schema(description = "产品ID")
    private String productId;

    /** 产品名称 */
    @Schema(description = "产品名称")
    private String productName;

    /** 数量 */
    @Schema(description = "数量")
    private Integer quantity;

    /** 单价 */
    @Schema(description = "单价")
    private BigDecimal unitPrice;

    /** 金额 */
    @Schema(description = "金额")
    private BigDecimal amount;
}
