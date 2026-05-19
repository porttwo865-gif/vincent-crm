package cn.vincent.crm.opportunity.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商机实体
 */
@Data
@Table(name = "opportunity")
public class Opportunity {

    /** 主键 ID */
    @Id
    private String id;

    /** 商机名称 */
    private String name;

    /** 关联客户 ID */
    private String customerId;

    /** 关联联系人 ID */
    private String contactId;

    /** 负责人 ID */
    private String owner;

    /** 阶段 ID */
    private String stage;

    /** 上次阶段 */
    private String lastStage;

    /** 预计金额 */
    private BigDecimal amount;

    /** 预计成交时间 */
    private Long expectedCloseTime;

    /** 备注 */
    private String remark;

    /** 看板排序位置 */
    private Long pos;

    /** 组织 ID */
    private String organizationId;

    /** 最新跟进人 */
    private String follower;

    /** 最新跟进时间 */
    private Long followTime;

    /** 创建人 */
    private String createUser;

    /** 更新人 */
    private String updateUser;

    /** 创建时间（时间戳） */
    private Long createTime;

    /** 更新时间（时间戳） */
    private Long updateTime;
}
