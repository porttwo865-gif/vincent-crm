package cn.vincent.crm.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 订单详情响应
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "订单详情响应")
public class OrderGetResponse extends OrderListResponse {

    /** 更新人姓名 */
    @Schema(description = "更新人姓名")
    private String updateUserName;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private Long updateTime;

    /** 订单明细列表 */
    @Schema(description = "订单明细列表")
    private List<OrderItemResponse> items;
}
