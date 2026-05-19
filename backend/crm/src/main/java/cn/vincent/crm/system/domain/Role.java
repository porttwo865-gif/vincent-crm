package cn.vincent.crm.system.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "sys_role")
public class Role extends BaseModel {

    /** 角色名称 */
    private String name;

    /** 角色描述 */
    private String description;

    /** 数据范围（ALL/DEPT_AND_CHILD/DEPT_CUSTOM/SELF） */
    private String dataScope;

    /** 是否启用 */
    private Boolean enable;

    /** 组织 ID */
    private String organizationId;
}
