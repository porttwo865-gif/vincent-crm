package cn.vincent.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 新增部门请求 DTO
 */
@Data
@Schema(description = "新增部门请求")
public class DepartmentAddRequest {

    /** 部门名称 */
    @NotBlank(message = "部门名称不能为空")
    @Schema(description = "部门名称")
    private String name;

    /** 父部门 ID */
    @Schema(description = "父部门ID")
    private String parentId;

    /** 排序 */
    @Schema(description = "排序")
    private Integer sort;
}
