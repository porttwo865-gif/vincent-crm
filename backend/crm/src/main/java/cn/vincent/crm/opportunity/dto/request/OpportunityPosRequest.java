package cn.vincent.crm.opportunity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 商机看板拖拽排序请求 DTO
 */
@Data
@Schema(description = "商机看板拖拽排序请求")
public class OpportunityPosRequest {

    /** 商机 ID */
    @NotBlank(message = "商机ID不能为空")
    @Schema(description = "商机ID")
    private String id;

    /** 排序位置 */
    @Schema(description = "排序位置")
    private Long pos;

    /** 阶段 ID（跨阶段拖拽时传递） */
    @Schema(description = "阶段ID（跨阶段拖拽时传递）")
    private String stage;
}
