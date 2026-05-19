package cn.vincent.crm.system.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "sys_department")
public class Department extends BaseModel {

    /** 部门名称 */
    private String name;

    /** 父部门 ID */
    private String parentId;

    /** 排序 */
    private Integer sort;

    /** 组织 ID */
    private String organizationId;
}
