package cn.vincent.crm.workbench.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 全局搜索响应 DTO
 */
@Data
@Schema(description = "全局搜索响应")
public class GlobalSearchResponse {

    /** 线索搜索结果 */
    @Schema(description = "线索搜索结果")
    private List<GlobalSearchClueItem> clues;

    /** 客户搜索结果 */
    @Schema(description = "客户搜索结果")
    private List<GlobalSearchCustomerItem> customers;

    /** 商机搜索结果 */
    @Schema(description = "商机搜索结果")
    private List<GlobalSearchOpportunityItem> opportunities;

    /** 合同搜索结果 */
    @Schema(description = "合同搜索结果")
    private List<GlobalSearchContractItem> contracts;

    /** 订单搜索结果 */
    @Schema(description = "订单搜索结果")
    private List<GlobalSearchOrderItem> orders;
}
