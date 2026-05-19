package cn.vincent.crm.follow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 跟进计划列表查询请求
 */
@Data
@Schema(description = "跟进计划列表查询请求")
public class FollowPlanListRequest {

    /** 业务类型：CLUE/CUSTOMER/OPPORTUNITY/CONTRACT */
    @NotBlank(message = "业务类型不能为空")
    @Schema(description = "业务类型：CLUE/CUSTOMER/OPPORTUNITY/CONTRACT")
    private String bizType;

    /** 业务对象 ID */
    @NotBlank(message = "业务对象ID不能为空")
    @Schema(description = "业务对象ID")
    private String bizId;
}
