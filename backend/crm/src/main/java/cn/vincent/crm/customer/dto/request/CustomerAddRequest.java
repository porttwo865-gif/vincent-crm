package cn.vincent.crm.customer.dto.request;

import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 新增客户请求 DTO
 */
@Data
@Schema(description = "新增客户请求")
public class CustomerAddRequest {

    /** 客户名称 */
    @NotBlank(message = "客户名称不能为空")
    @Schema(description = "客户名称")
    private String name;

    /** 负责人 ID */
    @Schema(description = "负责人ID")
    private String owner;

    /** 自定义字段值列表 */
    @Schema(description = "自定义字段值列表")
    private List<ModuleFieldValueDTO> moduleFields;
}
