package cn.vincent.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 新增用户请求 DTO
 */
@Data
@Schema(description = "新增用户请求")
public class UserAddRequest {

    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String username;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;

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
