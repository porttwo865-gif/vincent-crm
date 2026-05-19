package cn.vincent.crm.system.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模块表单定义实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "module_form")
public class ModuleForm extends BaseModel {

    /** 表单标识（对应 FormKey 枚举的 key） */
    private String formKey;

    /** 表单名称 */
    private String name;

    /** 组织 ID */
    private String organizationId;
}
