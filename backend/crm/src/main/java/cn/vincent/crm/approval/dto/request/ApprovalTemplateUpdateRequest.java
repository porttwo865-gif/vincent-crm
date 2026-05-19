package cn.vincent.crm.approval.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 更新审批模板请求
 */
@Data
@Schema(description = "更新审批模板请求")
public class ApprovalTemplateUpdateRequest {

    /** 模板 ID */
    @NotBlank(message = "模板ID不能为空")
    @Schema(description = "模板ID")
    private String id;

    /** 模板名称 */
    @Schema(description = "模板名称")
    private String name;

    /** 业务类型 */
    @Schema(description = "业务类型：CONTRACT/INVOICE/OTHER")
    private String bizType;

    /** 描述 */
    @Schema(description = "描述")
    private String description;

    /** 审批节点配置（JSON） */
    @Schema(description = "审批节点配置（JSON）")
    private String nodes;
}
