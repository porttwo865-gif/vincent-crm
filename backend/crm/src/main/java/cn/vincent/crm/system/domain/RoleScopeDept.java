package cn.vincent.crm.system.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 角色数据范围-部门关联实体
 */
@Data
@Table(name = "role_scope_dept")
public class RoleScopeDept {

    /** 主键 ID */
    @Id
    private String id;

    /** 角色 ID */
    private String roleId;

    /** 部门 ID */
    private String deptId;
}
