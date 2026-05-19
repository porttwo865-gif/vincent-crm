package cn.vincent.crm.contract.record.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 回款记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "crm_payment_record")
public class PaymentRecord extends BaseModel {

    /** 关联合同 ID */
    private String contractId;

    /** 关联回款计划 ID */
    private String planId;

    /** 回款金额 */
    private BigDecimal amount;

    /** 回款日期 */
    private Long paymentDate;

    /** 回款方式：transfer/cash/check/other */
    private String paymentMethod;

    /** 备注 */
    private String remark;
}
