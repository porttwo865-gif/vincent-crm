package cn.vincent.crm.approval.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 审批实例分页请求
 */
@Data
@Schema(description = "审批实例分页请求")
public class ApprovalInstancePageRequest {

    /** 当前页码 */
    @Schema(description = "当前页码")
    private Integer current = 1;

    /** 每页条数 */
    @Schema(description = "每页条数")
    private Integer pageSize = 20;

    /** 查询类型：mine/pending/all */
    @Schema(description = "查询类型：mine(我发起的)/pending(我审批的)/all(全部)")
    private String type;

    /** 业务类型 */
    @Schema(description = "业务类型")
    private String bizType;

    /** 状态 */
    @Schema(description = "状态")
    private String status;

    /** 搜索关键词 */
    @Schema(description = "搜索关键词")
    private String keyword;
}
