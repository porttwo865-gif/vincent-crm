package cn.vincent.crm.approval.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 审批模板分页请求
 */
@Data
@Schema(description = "审批模板分页请求")
public class ApprovalTemplatePageRequest {

    /** 当前页码 */
    @Schema(description = "当前页码")
    private Integer current = 1;

    /** 每页条数 */
    @Schema(description = "每页条数")
    private Integer pageSize = 20;

    /** 搜索关键词 */
    @Schema(description = "搜索关键词")
    private String keyword;

    /** 业务类型 */
    @Schema(description = "业务类型：CONTRACT/INVOICE/OTHER")
    private String bizType;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enabled;
}
