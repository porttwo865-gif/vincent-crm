package cn.vincent.crm.customer.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 客户联系人实体
 */
@Data
@Table(name = "customer_contact")
public class CustomerContact {

    /** 主键 ID */
    @Id
    private String id;

    /** 所属客户 ID */
    private String customerId;

    /** 联系人姓名 */
    private String name;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 职位 */
    private String position;

    /** 部门 */
    private String department;

    /** 是否主要联系人 */
    private Boolean isPrimary;

    /** 备注 */
    private String remark;

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
