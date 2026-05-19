package cn.vincent.crm.invoice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 发票状态变更请求
 */
@Data
@Schema(description = "发票状态变更请求")
public class InvoiceStatusRequest {

    /** 发票 ID */
    @NotBlank(message = "发票ID不能为空")
    @Schema(description = "发票ID")
    private String id;

    /** 状态 */
    @NotBlank(message = "状态不能为空")
    @Schema(description = "状态")
    private String status;
}
