package cn.vincent.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 条件过滤 DTO - 前端传入的条件筛选参数
 */
@Data
@Schema(description = "条件过滤")
public class ConditionDTO {

    /** 字段 ID */
    @Schema(description = "字段ID")
    private String fieldId;

    /** 操作符 */
    @Schema(description = "操作符")
    private String operator;

    /** 值 */
    @Schema(description = "值")
    private Object value;

    /** 字段类型 */
    @Schema(description = "字段类型")
    private String fieldType;

    /** 内部键 */
    @Schema(description = "内部键")
    private String internalKey;
}
