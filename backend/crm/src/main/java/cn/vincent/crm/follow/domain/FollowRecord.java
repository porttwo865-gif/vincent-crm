package cn.vincent.crm.follow.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 跟进记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "crm_follow_record")
public class FollowRecord extends BaseModel {

    /** 业务类型：CLUE/CUSTOMER/OPPORTUNITY/CONTRACT */
    private String bizType;

    /** 业务对象 ID */
    private String bizId;

    /** 跟进内容 */
    private String content;

    /** 跟进方式：phone/visit/email/wechat/other */
    private String followType;

    /** 下次跟进时间 */
    private Long nextFollowTime;

    /** 附件（JSON 字符串） */
    private String attachments;

    /** 负责人 ID */
    private String owner;

    /** 组织 ID */
    private String organizationId;
}
