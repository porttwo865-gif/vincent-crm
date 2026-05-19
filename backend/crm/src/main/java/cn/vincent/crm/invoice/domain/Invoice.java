package cn.vincent.crm.invoice.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 发票实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "crm_invoice")
public class Invoice extends BaseModel {

    /** 关联合同 ID */
    private String contractId;

    /** 关联客户 ID */
    private String customerId;

    /** 发票编号 */
    private String invoiceNo;

    /** 发票金额 */
    private BigDecimal amount;

    /** 开票日期 */
    private Long invoiceDate;

    /** 发票类型：normal/special */
    private String invoiceType;

    /** 状态：pending/issued/cancelled */
    private String status;

    /** 备注 */
    private String remark;

    /** 组织 ID */
    private String organizationId;
}
