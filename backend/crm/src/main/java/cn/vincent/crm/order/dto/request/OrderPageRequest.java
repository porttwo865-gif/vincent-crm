package cn.vincent.crm.order.dto.request;

import cn.vincent.common.util.ConditionFilterUtils;
import cn.vincent.common.dto.ConditionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 订单分页请求
 */
@Data
@Schema(description = "订单分页请求")
public class OrderPageRequest {

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

    /** 搜索关键词 */
    @Schema(description = "搜索关键词")
    private String keyword;

    /** 状态 */
    @Schema(description = "状态")
    private String status;

    /** 关联客户 ID */
    @Schema(description = "关联客户ID")
    private String customerId;
}
