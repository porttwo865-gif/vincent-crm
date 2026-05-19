package cn.vincent.crm.customer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 公海池分配客户请求 DTO
 */
@Data
@Schema(description = "公海池分配客户请求")
public class CustomerPoolAssignRequest {

    /** 客户 ID 列表 */
    @NotEmpty(message = "客户ID列表不能为空")
    @Schema(description = "客户ID列表")
    private List<String> ids;

    /** 指定负责人 ID */
    @NotBlank(message = "负责人不能为空")
    @Schema(description = "指定负责人ID")
    private String ownerId;
}
