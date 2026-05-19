package cn.vincent.crm.workbench.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 工作台最近动态响应 DTO
 */
@Data
@Schema(description = "工作台最近动态响应")
public class WorkbenchRecentResponse {

    /** 业务类型 */
    @Schema(description = "业务类型")
    private String bizType;

    /** 业务对象 ID */
    @Schema(description = "业务对象ID")
    private String bizId;

    /** 业务对象名称 */
    @Schema(description = "业务对象名称")
    private String bizName;

    /** 操作动作：created/updated/followed */
    @Schema(description = "操作动作：created/updated/followed")
    private String action;

    /** 操作时间 */
    @Schema(description = "操作时间")
    private Long operateTime;

    /** 操作人姓名 */
    @Schema(description = "操作人姓名")
    private String operatorName;
}
