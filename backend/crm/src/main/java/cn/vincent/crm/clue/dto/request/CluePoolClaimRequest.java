package cn.vincent.crm.clue.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 线索池领取请求
 */
@Data
@Schema(description = "线索池领取请求")
public class CluePoolClaimRequest {

    /** 线索 ID */
    @NotBlank(message = "线索ID不能为空")
    @Schema(description = "线索ID")
    private String clueId;
}
