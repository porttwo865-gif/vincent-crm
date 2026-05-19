package cn.vincent.crm.personal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 通知列表响应 DTO
 */
@Data
@Schema(description = "通知列表响应")
public class NotificationListResponse {

    /** 通知 ID */
    @Schema(description = "通知ID")
    private String id;

    /** 通知标题 */
    @Schema(description = "通知标题")
    private String title;

    /** 通知内容 */
    @Schema(description = "通知内容")
    private String content;

    /** 通知类型：system / approval / follow */
    @Schema(description = "通知类型")
    private String type;

    /** 业务类型 */
    @Schema(description = "业务类型")
    private String bizType;

    /** 业务 ID */
    @Schema(description = "业务ID")
    private String bizId;

    /** 阅读状态：unread / read */
    @Schema(description = "阅读状态：unread/read")
    private String readStatus;

    /** 创建时间（时间戳） */
    @Schema(description = "创建时间")
    private Long createTime;
}
