package cn.vincent.crm.invoice.dto.response;

import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 发票列表响应
 */
@Data
@Schema(description = "发票列表响应")
public class InvoiceListResponse {

    /** 发票 ID */
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
