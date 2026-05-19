package cn.vincent.crm.customer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 客户移入公海请求 DTO
 */
@Data
@Schema(description = "客户移入公海请求")
public class CustomerMovePoolRequest {

    /** 客户 ID 列表 */
    @NotEmpty(message = "客户ID列表不能为空")
    @Schema(description = "客户ID列表")
    private List<String> ids;

    /** 移入公海原因 ID */
    @Schema(description = "移入公海原因ID")
    private String reasonId;

    /** 移入公海原因 */
    @Schema(description = "移入公海原因")
    private String reason;
}
