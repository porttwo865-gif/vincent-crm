package cn.vincent.crm.invoice.dto.request;

import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 更新发票请求
 */
@Data
@Schema(description = "更新发票请求")
public class InvoiceUpdateRequest {

    /** 发票 ID */
    @NotBlank(message = "发票ID不能为空")
    @Schema(description = "发票ID")
    private String id;

    /** 关联合同 ID */
    @Schema(description = "关联合同ID")
    private String contractId;

    /** 关联客户 ID */
    @Schema(description = "关联客户ID")
    private String customerId;

    /** 发票编号 */
    @Schema(description = "发票编号")
    private String invoiceNo;

    /** 发票金额 */
    @Schema(description = "发票金额")
    private BigDecimal amount;

    /** 开票日期 */
    @Schema(description = "开票日期")
    private Long invoiceDate;

    /** 发票类型 */
    @Schema(description = "发票类型")
    private String invoiceType;

    /** 状态 */
    @Schema(description = "状态")
    private String status;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 自定义字段值列表 */
    @Schema(description = "自定义字段值列表")
    private List<ModuleFieldValueDTO> moduleFields;
}
