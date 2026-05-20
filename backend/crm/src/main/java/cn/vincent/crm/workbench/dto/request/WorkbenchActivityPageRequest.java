package cn.vincent.crm.workbench.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 工作台动态分页请求 DTO
 */
@Data
@Schema(description = "工作台动态分页请求")
public class WorkbenchActivityPageRequest {

    /** 页码 */
    @Schema(description = "页码")
    private int pageNum = 1;

    /** 每页数量 */
    @Schema(description = "每页数量")
    private int pageSize = 20;
}
