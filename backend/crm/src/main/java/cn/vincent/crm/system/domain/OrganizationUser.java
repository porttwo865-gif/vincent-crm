package cn.vincent.crm.system.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 组织用户关联实体
 */
@Data
@Table(name = "sys_organization_user")
public class OrganizationUser {

    /** 主键 ID */
    @Id
    private String id;

    /** 用户 ID */
    private String userId;

    /** 组织 ID */
    private String organizationId;

    /** 部门 ID */
    private String departmentId;
}
