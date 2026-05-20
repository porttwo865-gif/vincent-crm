package cn.vincent.crm.workbench.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 工作台待办条目响应 DTO
 */
@Data
@AllArgsConstructor
@Schema(description = "工作台待办条目")
public class WorkbenchTodoItemResponse {

    /** 待办类型标识 */
    @Schema(description = "待办类型标识")
    private String type;

    /** 待办标题 */
    @Schema(description = "待办标题")
    private String title;

    /** 数量 */
    @Schema(description = "数量")
    private int count;
}
