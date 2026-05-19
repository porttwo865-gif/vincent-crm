package cn.vincent.crm.clue.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 线索移入线索池请求
 */
@Data
@Schema(description = "线索移入线索池请求")
public class ClueMovePoolRequest {

    /** 线索 ID 列表 */
    @NotEmpty(message = "线索ID列表不能为空")
    @Schema(description = "线索ID列表")
    private List<String> ids;

    /** 移入原因 ID */
    @Schema(description = "移入原因ID")
    private String reasonId;

    /** 移入原因描述 */
    @Schema(description = "移入原因描述")
    private String reason;
}
