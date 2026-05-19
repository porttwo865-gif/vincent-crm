package cn.vincent.crm.product.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 产品实体
 */
@Data
@Table(name = "product")
public class Product {

    /** 主键 ID */
    @Id
    private String id;

    /** 产品名称 */
    private String name;

    /** 产品编码 */
    private String code;

    /** 分类 */
    private String category;

    /** 标准价格 */
    private BigDecimal price;

    /** 单位 */
    private String unit;

    /** 描述 */
    private String description;

    /** 是否启用 */
    private Boolean enable;

    /** 排序 */
    private Integer sort;

    /** 组织 ID */
    private String organizationId;

    /** 创建人 */
    private String createUser;

    /** 更新人 */
    private String updateUser;

    /** 创建时间（时间戳） */
    private Long createTime;

    /** 更新时间（时间戳） */
    private Long updateTime;
}
