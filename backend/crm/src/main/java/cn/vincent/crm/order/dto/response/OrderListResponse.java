package cn.vincent.crm.order.dto.response;

import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单列表响应
 */
@Data
@Schema(description = "订单列表响应")
public class OrderListResponse {

    /** 订单 ID */
    @Schema(description = "订单ID")
    private String id;

    /** 订单编号 */
    @Schema(description = "订单编号")
    private String orderNo;

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

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Long createTime;

    /** 创建人姓名 */
    @Schema(description = "创建人姓名")
    private String createUserName;

    /** 自定义字段值列表 */
    @Schema(description = "自定义字段值列表")
    private List<ModuleFieldValueDTO> moduleFields;
}
