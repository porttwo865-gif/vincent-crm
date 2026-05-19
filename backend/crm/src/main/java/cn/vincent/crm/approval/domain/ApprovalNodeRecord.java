package cn.vincent.crm.approval.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审批节点记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "crm_approval_node_record")
public class ApprovalNodeRecord extends BaseModel {

    /** 审批实例 ID */
    private String instanceId;

    /** 节点序号 */
    private Integer nodeSeq;

    /** 审批人 ID */
    private String approverId;

    /** 状态：pending/approved/rejected */
    private String status;

    /** 审批意见 */
    private String comment;

    /** 操作时间 */
    private Long operateTime;
}
