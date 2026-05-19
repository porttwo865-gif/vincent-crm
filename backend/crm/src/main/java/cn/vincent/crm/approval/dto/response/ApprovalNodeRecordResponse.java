package cn.vincent.crm.approval.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 审批节点记录响应
 */
@Data
@Schema(description = "审批节点记录响应")
public class ApprovalNodeRecordResponse {

    /** 记录 ID */
    @Schema(description = "记录ID")
    private String id;

    /** 审批实例 ID */
    @Schema(description = "审批实例ID")
    private String instanceId;

    /** 节点序号 */
    @Schema(description = "节点序号")
    private Integer nodeSeq;

    /** 审批人 ID */
    @Schema(description = "审批人ID")
    private String approverId;

    /** 审批人姓名 */
    @Schema(description = "审批人姓名")
    private String approverName;

    /** 状态 */
    @Schema(description = "状态：pending/approved/rejected")
    private String status;

    /** 审批意见 */
    @Schema(description = "审批意见")
    private String comment;

    /** 操作时间 */
    @Schema(description = "操作时间")
    private Long operateTime;
}
