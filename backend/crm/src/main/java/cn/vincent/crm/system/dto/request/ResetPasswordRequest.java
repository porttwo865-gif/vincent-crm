package cn.vincent.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 重置密码请求 DTO
 */
@Data
@Schema(description = "重置密码请求")
public class ResetPasswordRequest {

    /** 用户 ID */
    @NotBlank(message = "用户ID不能为空")
    @Schema(description = "用户ID")
    private String id;

    /** 新密码 */
    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码")
    private String newPassword;
}
