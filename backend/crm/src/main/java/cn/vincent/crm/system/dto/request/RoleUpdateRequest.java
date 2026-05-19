package cn.vincent.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 更新角色请求 DTO
 */
@Data
@Schema(description = "更新角色请求")
public class RoleUpdateRequest {

    /** 角色 ID */
    @NotBlank(message = "角色ID不能为空")
    @Schema(description = "角色ID")
    private String id;

    /** 角色名称 */
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
