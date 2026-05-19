package cn.vincent.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 新增角色请求 DTO
 */
@Data
@Schema(description = "新增角色请求")
public class RoleAddRequest {

    /** 角色名称 */
    @NotBlank(message = "角色名称不能为空")
    @Schema(description = "角色名称")
    private String name;

    /** 描述 */
    @Schema(description = "描述")
    private String description;

    /** 数据范围 */
    @Schema(description = "数据范围")
    private String dataScope;

    /** 自定义部门范围 */
    @Schema(description = "自定义部门范围")
    private List<String> deptIds;

    /** 权限 ID 列表 */
    @Schema(description = "权限ID列表")
    private List<String> permissionIds;
}
