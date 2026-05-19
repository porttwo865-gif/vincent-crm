package cn.vincent.crm.customer.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 客户实体
 */
@Data
@Table(name = "customer")
public class Customer {

    /** 主键 ID */
    @Id
    private String id;

    /** 客户名称 */
    private String name;

    /** 负责人 ID */
    private String owner;

    /** 领取时间 */
    private Long collectionTime;

    /** 公海池 ID */
    private String poolId;

    /** 是否在公海池 */
    private Boolean inSharedPool;

    /** 组织 ID */
    private String organizationId;

    /** 最新跟进人 */
    private String follower;

    /** 最新跟进时间 */
    private Long followTime;

    /** 移入公海原因 ID */
    private String reasonId;

    /** 创建人 */
    private String createUser;

    /** 更新人 */
    private String updateUser;

    /** 创建时间（时间戳） */
    private Long createTime;

    /** 更新时间（时间戳） */
    private Long updateTime;
}
