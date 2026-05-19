package cn.vincent.crm.opportunity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 更新商机阶段请求 DTO
 */
@Data
@Schema(description = "更新商机阶段请求")
public class OpportunityStageRequest {

    /** 商机 ID */
    @NotBlank(message = "商机ID不能为空")
    @Schema(description = "商机ID")
    private String id;

    /** 阶段 ID */
    @NotBlank(message = "阶段ID不能为空")
    @Schema(description = "阶段ID")
    private String stage;
}
