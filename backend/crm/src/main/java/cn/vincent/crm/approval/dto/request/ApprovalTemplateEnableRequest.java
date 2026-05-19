package cn.vincent.crm.approval.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 审批模板启用/禁用请求
 */
@Data
@Schema(description = "审批模板启用/禁用请求")
public class ApprovalTemplateEnableRequest {

    /** 模板 ID */
    @NotBlank(message = "模板ID不能为空")
    @Schema(description = "模板ID")
    private String id;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enabled;
}
