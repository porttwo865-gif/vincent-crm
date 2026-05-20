package cn.vincent.crm.workbench.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 工作台动态条目响应 DTO
 */
@Data
@Schema(description = "工作台动态条目")
public class WorkbenchActivityItemResponse {

    /** 记录 ID */
    @Schema(description = "记录ID")
    private String id;

    /** 动态内容描述 */
    @Schema(description = "动态内容描述")
    private String content;

    /** 发生时间戳 */
    @Schema(description = "发生时间戳")
    private Long time;

    /** 操作人姓名 */
    @Schema(description = "操作人姓名")
    private String userName;

    /** 业务类型 */
    @Schema(description = "业务类型")
    private String type;
}
