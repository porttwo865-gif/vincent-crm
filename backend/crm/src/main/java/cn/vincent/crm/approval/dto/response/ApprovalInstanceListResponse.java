package cn.vincent.crm.approval.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 审批实例列表响应
 */
@Data
@Schema(description = "审批实例列表响应")
public class ApprovalInstanceListResponse {

    /** 实例 ID */
    @Schema(description = "实例ID")
    private String id;

    /** 模板 ID */
    @Schema(description = "模板ID")
    private String templateId;

    /** 模板名称 */
    @Schema(description = "模板名称")
    private String templateName;

    /** 业务类型 */
    @Schema(description = "业务类型")
    private String bizType;

    /** 业务 ID */
    @Schema(description = "业务ID")
    private String bizId;

    /** 业务名称 */
    @Schema(description = "业务名称")
    private String bizName;

    /** 申请人 ID */
    @Schema(description = "申请人ID")
    private String applicant;

    /** 申请人姓名 */
    @Schema(description = "申请人姓名")
    private String applicantName;

    /** 状态 */
    @Schema(description = "状态：pending/approved/rejected/cancelled")
    private String status;

    /** 当前节点序号 */
    @Schema(description = "当前节点序号")
    private Integer currentNodeSeq;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Long createTime;
}
