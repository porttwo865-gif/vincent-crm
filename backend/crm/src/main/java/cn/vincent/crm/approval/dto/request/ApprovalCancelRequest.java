package cn.vincent.crm.approval.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 撤回审批请求
 */
@Data
@Schema(description = "撤回审批请求")
public class ApprovalCancelRequest {

    /** 审批实例 ID */
    @NotBlank(message = "审批实例ID不能为空")
    @Schema(description = "审批实例ID")
    private String instanceId;
}
