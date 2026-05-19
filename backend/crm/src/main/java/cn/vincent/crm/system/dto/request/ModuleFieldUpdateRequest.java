package cn.vincent.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 更新模块字段请求 DTO
 */
@Data
@Schema(description = "更新模块字段请求")
public class ModuleFieldUpdateRequest {

    /** 字段 ID */
    @NotBlank(message = "字段ID不能为空")
    @Schema(description = "字段ID")
    private String id;

    /** 字段显示名称 */
    @Schema(description = "字段显示名称")
    private String name;

    /** 字段类型 */
    @Schema(description = "字段类型")
    private String fieldType;

    /** 内部关联键 */
    @Schema(description = "内部关联键")
    private String internalKey;

    /** 是否必填 */
    @Schema(description = "是否必填")
    private Boolean required;

    /** 默认值 */
    @Schema(description = "默认值")
    private String defaultValue;

    /** 选项配置 JSON */
    @Schema(description = "选项配置JSON")
    private String options;

    /** 排序 */
    @Schema(description = "排序")
    private Integer sort;

    /** 是否可见 */
    @Schema(description = "是否可见")
    private Boolean visible;

    /** 是否可编辑 */
    @Schema(description = "是否可编辑")
    private Boolean editable;

    /** 所属分组名称 */
    @Schema(description = "所属分组名称")
    private String sectionName;

    /** 分组排序 */
    @Schema(description = "分组排序")
    private Integer sectionSort;
}
