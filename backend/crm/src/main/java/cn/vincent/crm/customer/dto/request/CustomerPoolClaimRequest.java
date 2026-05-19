package cn.vincent.crm.customer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 公海池领取客户请求 DTO
 */
@Data
@Schema(description = "公海池领取客户请求")
public class CustomerPoolClaimRequest {

    /** 客户 ID 列表 */
    @NotEmpty(message = "客户ID列表不能为空")
    @Schema(description = "客户ID列表")
    private List<String> ids;
}
