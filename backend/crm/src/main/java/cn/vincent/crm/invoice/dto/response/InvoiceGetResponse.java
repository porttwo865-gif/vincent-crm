package cn.vincent.crm.invoice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发票详情响应
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "发票详情响应")
public class InvoiceGetResponse extends InvoiceListResponse {

    /** 更新人姓名 */
    @Schema(description = "更新人姓名")
    private String updateUserName;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private Long updateTime;
}
