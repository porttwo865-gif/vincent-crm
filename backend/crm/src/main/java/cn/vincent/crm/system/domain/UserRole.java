package cn.vincent.crm.system.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 用户角色关联实体
 */
@Data
@Table(name = "sys_user_role")
public class UserRole {

    /** 主键 ID */
    @Id
    private String id;

    /** 用户 ID */
    private String userId;

    /** 角色 ID */
    private String roleId;

    /** 组织 ID */
    private String organizationId;
}
