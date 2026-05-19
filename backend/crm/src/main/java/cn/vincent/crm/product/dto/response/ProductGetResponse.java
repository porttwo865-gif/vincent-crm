package cn.vincent.crm.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品详情响应 DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "产品详情响应")
public class ProductGetResponse extends ProductListResponse {

    /** 描述 */
    @Schema(description = "描述")
    private String description;

    /** 创建人姓名 */
    @Schema(description = "创建人姓名")
    private String createUserName;

    /** 更新人姓名 */
    @Schema(description = "更新人姓名")
    private String updateUserName;
}
