package cn.vincent.crm.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 产品分页请求 DTO
 */
@Data
@Schema(description = "产品分页请求")
public class ProductPageRequest {

    /** 当前页码 */
    @Schema(description = "当前页码")
    private Integer current = 1;

    /** 每页条数 */
    @Schema(description = "每页条数")
    private Integer pageSize = 20;

    /** 搜索关键词（名称/编码） */
    @Schema(description = "搜索关键词")
    private String keyword;

    /** 分类 */
    @Schema(description = "分类")
    private String category;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;
}
