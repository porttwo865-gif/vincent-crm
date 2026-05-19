package cn.vincent.crm.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 订单状态变更请求
 */
@Data
@Schema(description = "订单状态变更请求")
public class OrderStatusRequest {

    /** 订单 ID */
    @NotBlank(message = "订单ID不能为空")
    @Schema(description = "订单ID")
    private String id;

    /** 状态 */
    @NotBlank(message = "状态不能为空")
    @Schema(description = "状态")
    private String status;
}
