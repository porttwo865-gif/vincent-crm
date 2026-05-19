package cn.vincent.crm.contract.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 合同实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "crm_contract")
public class Contract extends BaseModel {

    /** 合同名称 */
    private String name;

    /** 关联客户 ID */
    private String customerId;

    /** 关联商机 ID */
    private String opportunityId;

    /** 负责人 ID */
    private String owner;

    /** 合同金额 */
    private BigDecimal amount;

    /** 开始日期 */
    private Long startDate;

    /** 结束日期 */
    private Long endDate;

    /** 签约日期 */
    private Long signedDate;

    /** 状态：draft/active/completed/terminated */
    private String status;

    /** 备注 */
    private String remark;

    /** 排序位置 */
    private Long pos;

    /** 组织 ID */
    private String organizationId;
}
