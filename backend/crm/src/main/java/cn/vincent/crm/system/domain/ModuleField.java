package cn.vincent.crm.system.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模块字段定义实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "module_field")
public class ModuleField extends BaseModel {

    /** 所属表单 ID */
    private String formId;

    /** 所属表单 key */
    private String formKey;

    /** 字段显示名称 */
    private String name;

    /** 字段标识（英文） */
    private String fieldKey;

    /** 字段类型（FieldType 枚举的 code） */
    private String fieldType;

    /** 内部关联键（固定字段映射到实体属性名） */
    private String internalKey;

    /** 是否系统内置字段 */
    private Boolean isSystem;

    /** 是否必填 */
    private Boolean required;

    /** 默认值 */
    private String defaultValue;

    /** 选项配置（JSON 格式，用于 SELECT/MULTI_SELECT 等） */
    private String options;

    /** 排序 */
    private Integer sort;

    /** 是否可见 */
    private Boolean visible;

    /** 是否可编辑 */
    private Boolean editable;

    /** 所属分组名称 */
    private String sectionName;

    /** 分组排序 */
    private Integer sectionSort;

    /** 组织 ID */
    private String organizationId;
}
