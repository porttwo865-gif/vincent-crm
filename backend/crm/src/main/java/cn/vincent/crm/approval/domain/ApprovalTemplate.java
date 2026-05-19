package cn.vincent.crm.approval.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审批模板实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "crm_approval_template")
public class ApprovalTemplate extends BaseModel {

    /** 模板名称 */
    private String name;

    /** 业务类型：CONTRACT/INVOICE/OTHER */
    private String bizType;

    /** 描述 */
    private String description;

    /** 是否启用 */
    private Boolean enabled;

    /** 审批节点配置（JSON） */
    private String nodes;

    /** 组织 ID */
    private String organizationId;
}
