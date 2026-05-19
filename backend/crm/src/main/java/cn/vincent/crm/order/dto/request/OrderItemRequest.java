package cn.vincent.crm.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单明细请求
 */
@Data
@Schema(description = "订单明细请求")
public class OrderItemRequest {

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
