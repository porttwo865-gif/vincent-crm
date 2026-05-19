package cn.vincent.crm.customer.dto.request;

import cn.vincent.common.dto.ConditionDTO;
import cn.vincent.common.util.ConditionFilterUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 客户分页请求 DTO
 */
@Data
@Schema(description = "客户分页请求")
public class CustomerPageRequest {

    /** 当前页码 */
    @Schema(description = "当前页码")
    private Integer current = 1;

    /** 每页条数 */
    @Schema(description = "每页条数")
    private Integer pageSize = 20;

    /** 视图 ID */
    @Schema(description = "视图ID")
    private String viewId;

    /** 条件筛选列表 */
    @Schema(description = "条件筛选列表")
    private List<ConditionDTO> conditions;

    /** 解析后的条件（框架内部使用） */
    private List<ConditionFilterUtils.ParsedCondition> parsedConditions;

    /** 是否在公海池 */
    @Schema(description = "是否在公海池")
    private Boolean inSharedPool;
}
