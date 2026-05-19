package cn.vincent.crm.personal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录日志列表响应 DTO
 */
@Data
@Schema(description = "登录日志列表响应")
public class LoginLogListResponse {

    /** 日志 ID */
    @Schema(description = "日志ID")
    private String id;

    /** 登录时间（时间戳） */
    @Schema(description = "登录时间")
    private Long loginTime;

    /** 登录 IP */
    @Schema(description = "登录IP")
    private String ip;

    /** 浏览器/客户端信息 */
    @Schema(description = "浏览器/客户端信息")
    private String userAgent;

    /** 状态：success / failed */
    @Schema(description = "状态：success/failed")
    private String status;
}
