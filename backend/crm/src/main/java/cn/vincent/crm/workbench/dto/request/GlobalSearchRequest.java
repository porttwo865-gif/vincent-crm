package cn.vincent.crm.workbench.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 全局搜索请求 DTO
 */
@Data
@Schema(description = "全局搜索请求")
public class GlobalSearchRequest {

    /** 关键词 */
    @Schema(description = "关键词")
    private String keyword;

    /** 限定搜索范围: CLUE/CUSTOMER/OPPORTUNITY/CONTRACT/ORDER */
    @Schema(description = "限定搜索范围: CLUE/CUSTOMER/OPPORTUNITY/CONTRACT/ORDER")
    private List<String> types;
}
