package cn.vincent.crm.product.dto.response;

import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品列表响应 DTO
 */
@Data
@Schema(description = "产品列表响应")
public class ProductListResponse {

    /** 产品 ID */
    @Schema(description = "产品ID")
    private String id;

    /** 产品名称 */
    @Schema(description = "产品名称")
    private String name;

    /** 产品编码 */
    @Schema(description = "产品编码")
    private String code;

    /** 分类 */
    @Schema(description = "分类")
    private String category;

    /** 标准价格 */
    @Schema(description = "标准价格")
    private BigDecimal price;

    /** 单位 */
    @Schema(description = "单位")
    private String unit;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /** 排序 */
    @Schema(description = "排序")
    private Integer sort;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Long createTime;

    /** 自定义字段值列表 */
    @Schema(description = "自定义字段值列表")
    private List<ModuleFieldValueDTO> moduleFields;
}
