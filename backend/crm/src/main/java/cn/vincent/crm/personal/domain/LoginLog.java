package cn.vincent.crm.personal.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录日志实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "crm_login_log")
public class LoginLog extends BaseModel {

    /** 用户 ID */
    private String userId;

    /** 用户名 */
    private String username;

    /** 登录时间（时间戳） */
    private Long loginTime;

    /** 登录 IP */
    private String ip;

    /** 浏览器/客户端信息 */
    private String userAgent;

    /** 状态：success / failed */
    private String status;
}
