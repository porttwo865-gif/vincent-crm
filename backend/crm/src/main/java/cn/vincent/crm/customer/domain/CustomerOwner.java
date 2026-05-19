package cn.vincent.crm.customer.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 客户负责人变更历史实体
 */
@Data
@Table(name = "customer_owner")
public class CustomerOwner {

    /** 主键 ID */
    @Id
    private String id;

    /** 客户 ID */
    private String customerId;

    /** 原负责人 */
    private String fromOwner;

    /** 新负责人 */
    private String toOwner;

    /** 操作类型: TRANSFER/CLAIM/ASSIGN/MOVE_POOL */
    private String operationType;

    /** 操作人 */
    private String operatorId;

    /** 操作时间 */
    private Long operateTime;

    /** 组织 ID */
    private String organizationId;
}
