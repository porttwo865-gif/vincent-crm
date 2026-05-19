package cn.vincent.crm.personal.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 标记通知已读请求 DTO
 */
@Data
@Schema(description = "标记通知已读请求")
public class NotificationReadRequest {

    /** 通知 ID 列表 */
    @NotEmpty(message = "通知ID列表不能为空")
    @Schema(description = "通知ID列表")
    private List<String> ids;
}
