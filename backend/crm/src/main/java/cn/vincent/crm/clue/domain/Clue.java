package cn.vincent.crm.clue.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 线索实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "clue")
public class Clue extends BaseModel {

    /** 客户名称 */
    private String name;

    /** 负责人 ID */
    private String owner;

    /** 阶段 ID */
    private String stage;

    /** 上次阶段 */
    private String lastStage;

    /** 联系人名称 */
    private String contact;

    /** 联系人电话 */
    private String phone;

    /** 意向产品 ID（JSON 数组） */
    private String products;

    /** 组织 ID */
    private String organizationId;

    /** 领取时间 */
    private Long collectionTime;

    /** 是否在线索池 */
    private Boolean inSharedPool;

    /** 转化类型（CUSTOMER） */
    private String transitionType;

    /** 转化目标 ID */
    private String transitionId;

    /** 最新跟进人 */
    private String follower;

    /** 最新跟进时间 */
    private Long followTime;

    /** 线索池 ID */
    private String poolId;

    /** 移入线索池原因 ID */
    private String reasonId;
}
