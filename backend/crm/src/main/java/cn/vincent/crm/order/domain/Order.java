package cn.vincent.crm.order.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 订单实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "crm_order")
public class Order extends BaseModel {

    /** 订单编号 */
    private String orderNo;

    /** 关联客户 ID */
    private String customerId;

    /** 关联联系人 ID */
    private String contactId;

    /** 关联合同 ID */
    private String contractId;

    /** 订单总金额 */
    private BigDecimal totalAmount;

    /** 状态：draft/confirmed/shipped/completed/cancelled */
    private String status;

    /** 备注 */
    private String remark;

    /** 组织 ID */
    private String organizationId;
}
