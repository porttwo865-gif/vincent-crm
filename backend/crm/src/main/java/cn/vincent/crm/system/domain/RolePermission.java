package cn.vincent.crm.system.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 角色权限关联实体
 */
@Data
@Table(name = "sys_role_permission")
public class RolePermission {

    /** 主键 ID */
    @Id
    private String id;

    /** 角色 ID */
    private String roleId;

    /** 权限 ID */
    private String permissionId;
}
