package cn.vincent.crm.system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色列表响应 DTO
 */
@Data
@Schema(description = "角色列表响应")
public class RoleListResponse {

    /** 角色 ID */
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

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /** 关联用户数 */
    @Schema(description = "关联用户数")
    private Integer userCount;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Long createTime;
}
