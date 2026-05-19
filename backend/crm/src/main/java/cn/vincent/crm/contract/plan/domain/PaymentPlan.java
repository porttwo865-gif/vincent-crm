package cn.vincent.crm.contract.plan.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 回款计划实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "crm_payment_plan")
public class PaymentPlan extends BaseModel {

    /** 关联合同 ID */
    private String contractId;

    /** 期数 */
    private Integer planNum;

    /** 计划金额 */
    private BigDecimal amount;

    /** 预计回款日期 */
    private Long expectedDate;

    /** 实际回款日期 */
    private Long actualDate;

    /** 状态：pending/received/overdue */
    private String status;

    /** 备注 */
    private String remark;
}
