package cn.vincent.crm.approval.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 发起审批请求
 */
@Data
@Schema(description = "发起审批请求")
public class ApprovalSubmitRequest {

    /** 业务类型 */
    @NotBlank(message = "业务类型不能为空")
    @Schema(description = "业务类型：CONTRACT/INVOICE/OTHER")
    private String bizType;

    /** 业务 ID */
    @NotBlank(message = "业务ID不能为空")
    @Schema(description = "业务ID")
    private String bizId;

    /** 业务名称 */
    @NotBlank(message = "业务名称不能为空")
    @Schema(description = "业务名称")
    private String bizName;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
