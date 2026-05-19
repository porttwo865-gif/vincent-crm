package cn.vincent.crm.system.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "sys_organization")
public class Organization extends BaseModel {

    /** 组织名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 是否启用 */
    private Boolean enable;
}
