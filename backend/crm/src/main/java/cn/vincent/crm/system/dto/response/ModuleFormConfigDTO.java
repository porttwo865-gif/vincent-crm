package cn.vincent.crm.system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 模块表单配置响应 DTO
 */
@Data
@Schema(description = "模块表单配置")
public class ModuleFormConfigDTO {

    /** 表单 ID */
    @Schema(description = "表单ID")
    private String formId;

    /** 表单 key */
    @Schema(description = "表单key")
    private String formKey;

    /** 表单名称 */
    @Schema(description = "表单名称")
    private String formName;

    /** 字段列表 */
    @Schema(description = "字段列表")
    private List<ModuleFieldDTO> fields;
}
