package cn.vincent.security.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 会话用户信息 - 存储在 Shiro Session 中的用户主体
 */
@Data
public class SessionUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 用户 ID */
    private String userId;

    /** 用户名 */
    private String username;

    /** 姓名 */
    private String name;

    /** 组织 ID */
    private String organizationId;

    /** 权限标识列表 */
    private List<String> permissions;

    /** 角色 ID 列表 */
    private List<String> roleIds;
}
