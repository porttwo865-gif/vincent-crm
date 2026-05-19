package cn.vincent.crm.approval.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 审批操作请求（通过/驳回）
 */
@Data
@Schema(description = "审批操作请求")
public class ApprovalOperateRequest {

    /** 审批实例 ID */
    @NotBlank(message = "审批实例ID不能为空")
    @Schema(description = "审批实例ID")
    private String instanceId;

    /** 审批意见 */
    @Schema(description = "审批意见")
    private String comment;
}
