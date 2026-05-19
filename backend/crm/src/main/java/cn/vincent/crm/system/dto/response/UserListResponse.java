package cn.vincent.crm.system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 用户列表响应 DTO
 */
@Data
@Schema(description = "用户列表响应")
public class UserListResponse {

    /** 用户 ID */
    @Schema(description = "用户ID")
    private String id;

    /** 用户名 */
    @Schema(description = "用户名")
    private String username;

    /** 姓名 */
    @Schema(description = "姓名")
    private String name;

    /** 邮箱 */
    @Schema(description = "邮箱")
    private String email;

    /** 手机号 */
    @Schema(description = "手机号")
    private String phone;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /** 部门 ID */
    @Schema(description = "部门ID")
    private String departmentId;

    /** 部门名称 */
    @Schema(description = "部门名称")
    private String departmentName;

    /** 角色名称列表 */
    @Schema(description = "角色名称列表")
    private List<String> roleNames;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Long createTime;
}
