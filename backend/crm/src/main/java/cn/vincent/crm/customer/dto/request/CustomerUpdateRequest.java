package cn.vincent.crm.customer.dto.request;

import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 更新客户请求 DTO
 */
@Data
@Schema(description = "更新客户请求")
public class CustomerUpdateRequest {

    /** 客户 ID */
    @NotBlank(message = "客户ID不能为空")
    @Schema(description = "客户ID")
    private String id;

    /** 客户名称 */
    @Schema(description = "客户名称")
    private String name;

    /** 自定义字段值列表 */
    @Schema(description = "自定义字段值列表")
    private List<ModuleFieldValueDTO> moduleFields;
}
