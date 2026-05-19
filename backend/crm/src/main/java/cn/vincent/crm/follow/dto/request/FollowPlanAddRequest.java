package cn.vincent.crm.follow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 添加跟进计划请求
 */
@Data
@Schema(description = "添加跟进计划请求")
public class FollowPlanAddRequest {

    /** 业务类型：CLUE/CUSTOMER/OPPORTUNITY/CONTRACT */
    @NotBlank(message = "业务类型不能为空")
    @Schema(description = "业务类型：CLUE/CUSTOMER/OPPORTUNITY/CONTRACT")
    private String bizType;

    /** 业务对象 ID */
    @NotBlank(message = "业务对象ID不能为空")
    @Schema(description = "业务对象ID")
    private String bizId;

    /** 计划跟进时间 */
    @NotNull(message = "计划跟进时间不能为空")
    @Schema(description = "计划跟进时间")
    private Long planTime;

    /** 计划内容 */
    @NotBlank(message = "计划内容不能为空")
    @Schema(description = "计划内容")
    private String content;

    /** 提前提醒分钟数 */
    @Schema(description = "提前提醒分钟数")
    private Integer remindBefore;
}
