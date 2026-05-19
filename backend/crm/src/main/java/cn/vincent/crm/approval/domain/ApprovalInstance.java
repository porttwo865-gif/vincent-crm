package cn.vincent.crm.approval.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审批实例实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "crm_approval_instance")
public class ApprovalInstance extends BaseModel {

    /** 模板 ID */
    private String templateId;

    /** 模板名称 */
    private String templateName;

    /** 业务类型 */
    private String bizType;

    /** 业务 ID */
    private String bizId;

    /** 业务名称 */
    private String bizName;

    /** 申请人 ID */
    private String applicant;

    /** 状态：pending/approved/rejected/cancelled */
    private String status;

    /** 当前节点序号 */
    private Integer currentNodeSeq;

    /** 备注 */
    private String remark;

    /** 组织 ID */
    private String organizationId;
}
