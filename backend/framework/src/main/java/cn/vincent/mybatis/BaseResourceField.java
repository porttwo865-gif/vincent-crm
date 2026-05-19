package cn.vincent.mybatis;

import jakarta.persistence.Id;
import lombok.Data;

/**
 * 资源自定义字段值基类 - 各业务模块字段值表的公共字段
 */
@Data
public class BaseResourceField {

    /** 主键 ID */
    @Id
    private String id;

    /** 关联业务实体 ID */
    private String resourceId;

    /** 关联 ModuleField ID */
    private String fieldId;

    /** 字段类型 */
    private String fieldType;

    /** 字段名称 */
    private String name;

    /** 内部键 */
    private String internalKey;

    /** 字段值 */
    private String value;

    /** 创建人 */
    private String createUser;

    /** 创建时间（时间戳） */
    private Long createTime;
}
