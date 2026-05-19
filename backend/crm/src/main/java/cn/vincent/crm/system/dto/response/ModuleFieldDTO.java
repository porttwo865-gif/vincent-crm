package cn.vincent.crm.system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 模块字段响应 DTO
 */
@Data
@Schema(description = "模块字段")
public class ModuleFieldDTO {

    /** 字段 ID */
    @Schema(description = "字段ID")
    private String id;

    /** 字段标识 */
    @Schema(description = "字段标识")
    private String fieldKey;

    /** 字段名称 */
    @Schema(description = "字段名称")
    private String name;

    /** 字段类型 */
    @Schema(description = "字段类型")
    private String fieldType;

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

    /** 是否系统字段 */
    @Schema(description = "是否系统字段")
    private Boolean isSystem;

    /** 是否可见 */
    @Schema(description = "是否可见")
    private Boolean visible;

    /** 是否可编辑 */
    @Schema(description = "是否可编辑")
    private Boolean editable;

    /** 分组名称 */
    @Schema(description = "分组名称")
    private String sectionName;

    /** 分组排序 */
    @Schema(description = "分组排序")
    private Integer sectionSort;

    /** 内部关联键 */
    @Schema(description = "内部关联键")
    private String internalKey;
}
