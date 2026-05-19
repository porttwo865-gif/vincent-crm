package cn.vincent.mybatis;

import jakarta.persistence.Id;
import lombok.Data;

/**
 * 通用基础模型 - 所有实体的公共字段
 */
@Data
public class BaseModel {

    /** 主键 ID */
    @Id
    private String id;

    /** 创建人 */
    private String createUser;

    /** 更新人 */
    private String updateUser;

    /** 创建时间（时间戳） */
    private Long createTime;

    /** 更新时间（时间戳） */
    private Long updateTime;
}
