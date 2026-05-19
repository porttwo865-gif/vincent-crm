package cn.vincent.crm.personal.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改个人信息请求 DTO
 */
@Data
@Schema(description = "修改个人信息请求")
public class PersonalUpdateRequest {

    /** 姓名（可选） */
    @Schema(description = "姓名")
    private String name;

    /** 手机号（可选） */
    @Schema(description = "手机号")
    private String phone;

    /** 邮箱（可选） */
    @Schema(description = "邮箱")
    private String email;

    /** 头像地址（可选） */
    @Schema(description = "头像地址")
    private String avatar;
}
