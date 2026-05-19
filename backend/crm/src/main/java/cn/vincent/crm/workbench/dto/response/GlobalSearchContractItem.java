package cn.vincent.crm.workbench.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 全局搜索合同结果项
 */
@Data
@Schema(description = "全局搜索合同结果项")
public class GlobalSearchContractItem {

    /** 合同 ID */
    @Schema(description = "合同ID")
    private String id;

    /** 合同名称 */
    @Schema(description = "合同名称")
    private String name;

    /** 关联客户名称 */
    @Schema(description = "关联客户名称")
    private String customerName;

    /** 合同金额 */
    @Schema(description = "合同金额")
    private BigDecimal amount;

    /** 状态 */
    @Schema(description = "状态")
    private String status;
}
