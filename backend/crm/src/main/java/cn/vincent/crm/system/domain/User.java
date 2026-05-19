package cn.vincent.crm.system.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统用户实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "sys_user")
public class User extends BaseModel {

    /** 用户名 */
    private String username;

    /** 密码（bcrypt 加密） */
    private String password;

    /** 姓名 */
    private String name;

    /** 邮箱 */
    private String email;

    /** 手机号 */
    private String phone;

    /** 头像地址 */
    private String avatar;

    /** 是否启用 */
    private Boolean enable;

    /** 组织 ID */
    private String organizationId;
}
