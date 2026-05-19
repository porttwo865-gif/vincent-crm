package cn.vincent.crm.personal.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息通知实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "crm_notification")
public class Notification extends BaseModel {

    /** 接收用户 ID */
    private String userId;

    /** 通知标题 */
    private String title;

    /** 通知内容 */
    private String content;

    /** 通知类型：system / approval / follow */
    private String type;

    /** 业务类型 */
    private String bizType;

    /** 业务 ID */
    private String bizId;

    /** 阅读状态：unread / read */
    private String readStatus;
}
