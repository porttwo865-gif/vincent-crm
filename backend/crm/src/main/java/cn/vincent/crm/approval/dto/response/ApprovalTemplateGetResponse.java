package cn.vincent.crm.approval.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 审批模板详情响应
 */
@Data
@Schema(description = "审批模板详情响应")
public class ApprovalTemplateGetResponse {

    /** 模板 ID */
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

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enabled;

    /** 审批节点配置（JSON） */
    @Schema(description = "审批节点配置（JSON）")
    private String nodes;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Long createTime;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private Long updateTime;

    /** 创建人姓名 */
    @Schema(description = "创建人姓名")
    private String createUserName;

    /** 更新人姓名 */
    @Schema(description = "更新人姓名")
    private String updateUserName;
}
