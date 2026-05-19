package cn.vincent.crm.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求 DTO
 */
@Data
@Schema(description = "登录请求")
public class LoginRequest {

    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String username;

    /** 密码（RSA 加密） */
    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码（RSA 加密）")
    private String password;

    /** RSA 密钥标识（用于服务端解密） */
    @NotBlank(message = "密钥标识不能为空")
    @Schema(description = "RSA 密钥标识")
    private String rsaKey;
}
