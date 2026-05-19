package cn.vincent.crm.workbench.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 全局搜索商机结果项
 */
@Data
@Schema(description = "全局搜索商机结果项")
public class GlobalSearchOpportunityItem {

    /** 商机 ID */
    @Schema(description = "商机ID")
    private String id;

    /** 商机名称 */
    @Schema(description = "商机名称")
    private String name;

    /** 关联客户名称 */
    @Schema(description = "关联客户名称")
    private String customerName;

    /** 预计金额 */
    @Schema(description = "预计金额")
    private BigDecimal amount;

    /** 阶段 ID */
    @Schema(description = "阶段ID")
    private String stage;
}
