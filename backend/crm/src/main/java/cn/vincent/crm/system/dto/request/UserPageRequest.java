package cn.vincent.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户分页请求 DTO
 */
@Data
@Schema(description = "用户分页请求")
public class UserPageRequest {

    /** 当前页码 */
    @Schema(description = "当前页码")
    private Integer current = 1;

    /** 每页条数 */
    @Schema(description = "每页条数")
    private Integer pageSize = 20;

    /** 搜索关键词 */
    @Schema(description = "搜索关键词")
    private String keyword;

    /** 部门 ID */
    @Schema(description = "部门ID")
    private String departmentId;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;
}
