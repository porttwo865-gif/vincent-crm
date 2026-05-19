package cn.vincent.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 更新用户请求 DTO
 */
@Data
@Schema(description = "更新用户请求")
public class UserUpdateRequest {

    /** 用户 ID */
    @NotBlank(message = "用户ID不能为空")
    @Schema(description = "用户ID")
    private String id;

    /** 姓名 */
    @Schema(description = "姓名")
    private String name;

    /** 邮箱 */
    @Schema(description = "邮箱")
    private String email;

    /** 手机号 */
    @Schema(description = "手机号")
    private String phone;

    /** 部门 ID */
    @Schema(description = "部门ID")
    private String departmentId;

    /** 角色 ID 列表 */
    @Schema(description = "角色ID列表")
    private List<String> roleIds;
}
