package cn.vincent.crm.product.dto.request;

import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 更新产品请求 DTO
 */
@Data
@Schema(description = "更新产品请求")
public class ProductUpdateRequest {

    /** 产品 ID */
    @NotBlank(message = "产品ID不能为空")
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

    /** 描述 */
    @Schema(description = "描述")
    private String description;

    /** 自定义字段值列表 */
    @Schema(description = "自定义字段值列表")
    private List<ModuleFieldValueDTO> moduleFields;
}
