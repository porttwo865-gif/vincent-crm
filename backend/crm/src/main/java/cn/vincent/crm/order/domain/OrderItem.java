package cn.vincent.crm.order.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 订单明细实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "crm_order_item")
public class OrderItem extends BaseModel {

    /** 关联订单 ID */
    private String orderId;

    /** 产品 ID */
    private String productId;

    /** 产品名称 */
    private String productName;

    /** 数量 */
    private Integer quantity;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 金额 */
    private BigDecimal amount;
}
