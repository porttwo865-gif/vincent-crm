package cn.vincent.crm.personal.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 通知分页请求 DTO
 */
@Data
@Schema(description = "通知分页请求")
public class NotificationPageRequest {

    /** 当前页码 */
    @Schema(description = "当前页码")
    private Integer current = 1;

    /** 每页条数 */
    @Schema(description = "每页条数")
    private Integer pageSize = 20;

    /** 通知类型（可选）：system / approval / follow */
    @Schema(description = "通知类型：system/approval/follow")
    private String type;
}
