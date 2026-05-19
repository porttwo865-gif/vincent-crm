package cn.vincent.crm.order.dto.request;

import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 新增订单请求
 */
@Data
@Schema(description = "新增订单请求")
public class OrderAddRequest {

    /** 关联客户 ID */
    @Schema(description = "关联客户ID")
    private String customerId;

    /** 关联联系人 ID */
    @Schema(description = "关联联系人ID")
    private String contactId;

    /** 关联合同 ID */
    @Schema(description = "关联合同ID")
    private String contractId;

    /** 订单总金额 */
    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    /** 状态 */
    @Schema(description = "状态")
    private String status;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 订单明细列表 */
    @Schema(description = "订单明细列表")
    private List<OrderItemRequest> items;

    /** 自定义字段值列表 */
    @Schema(description = "自定义字段值列表")
    private List<ModuleFieldValueDTO> moduleFields;
}
