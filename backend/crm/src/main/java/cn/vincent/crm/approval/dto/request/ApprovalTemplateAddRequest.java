package cn.vincent.crm.approval.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 新增审批模板请求
 */
@Data
@Schema(description = "新增审批模板请求")
public class ApprovalTemplateAddRequest {

    /** 模板名称 */
    @NotBlank(message = "模板名称不能为空")
    @Schema(description = "模板名称")
    private String name;

    /** 业务类型 */
    @NotBlank(message = "业务类型不能为空")
    @Schema(description = "业务类型：CONTRACT/INVOICE/OTHER")
    private String bizType;

    /** 描述 */
    @Schema(description = "描述")
    private String description;

    /** 审批节点配置（JSON） */
    @NotBlank(message = "审批节点配置不能为空")
    @Schema(description = "审批节点配置（JSON）")
    private String nodes;
}
