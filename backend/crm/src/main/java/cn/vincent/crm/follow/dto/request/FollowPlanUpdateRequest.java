package cn.vincent.crm.follow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 更新跟进计划请求
 */
@Data
@Schema(description = "更新跟进计划请求")
public class FollowPlanUpdateRequest {

    /** 跟进计划 ID */
    @NotBlank(message = "跟进计划ID不能为空")
    @Schema(description = "跟进计划ID")
    private String id;

    /** 计划跟进时间 */
    @Schema(description = "计划跟进时间")
    private Long planTime;

    /** 计划内容 */
    @Schema(description = "计划内容")
    private String content;

    /** 提前提醒分钟数 */
    @Schema(description = "提前提醒分钟数")
    private Integer remindBefore;
}
