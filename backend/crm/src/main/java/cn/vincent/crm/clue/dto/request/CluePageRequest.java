package cn.vincent.crm.clue.dto.request;

import cn.vincent.common.dto.ConditionDTO;
import cn.vincent.common.util.ConditionFilterUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 线索分页查询请求
 */
@Data
@Schema(description = "线索分页查询请求")
public class CluePageRequest {

    /** 当前页码 */
    @Schema(description = "当前页码")
    private Integer current = 1;

    /** 每页条数 */
    @Schema(description = "每页条数")
    private Integer pageSize = 20;

    /** 视图 ID */
    @Schema(description = "视图ID")
    private String viewId;

    /** 条件筛选 */
    @Schema(description = "条件筛选")
    private List<ConditionDTO> conditions;

    /** 是否在线索池（null=全部，true=线索池，false=我的线索） */
    @Schema(description = "是否在线索池（null=全部，true=线索池，false=我的线索）")
    private Boolean inSharedPool;

    /** 关键词搜索 */
    @Schema(description = "关键词搜索")
    private String keyword;

    /** 解析后的条件（内部使用） */
    private List<ConditionFilterUtils.ParsedCondition> parsedConditions;
}
