package cn.vincent.crm.follow.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 跟进计划实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "crm_follow_plan")
public class FollowPlan extends BaseModel {

    /** 业务类型：CLUE/CUSTOMER/OPPORTUNITY/CONTRACT */
    private String bizType;

    /** 业务对象 ID */
    private String bizId;

    /** 计划跟进时间 */
    private Long planTime;

    /** 计划内容 */
    private String content;

    /** 提前提醒分钟数 */
    private Integer remindBefore;

    /** 状态：pending/done/expired */
    private String status;

    /** 负责人 ID */
    private String owner;

    /** 组织 ID */
    private String organizationId;
}
