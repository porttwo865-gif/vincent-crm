package cn.vincent.crm.system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 模块字段值响应 DTO
 */
@Data
@Schema(description = "模块字段值")
public class ModuleFieldValueDTO {

    /** 字段 ID */
    @Schema(description = "字段ID")
    private String fieldId;

    /** 字段名称 */
    @Schema(description = "字段名称")
    private String name;

    /** 字段值 */
    @Schema(description = "字段值")
    private String value;

    /** 字段类型 */
    @Schema(description = "字段类型")
    private String fieldType;
}
