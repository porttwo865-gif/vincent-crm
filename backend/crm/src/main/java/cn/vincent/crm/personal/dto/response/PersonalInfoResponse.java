package cn.vincent.crm.personal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 个人信息响应 DTO
 */
@Data
@Schema(description = "个人信息响应")
public class PersonalInfoResponse {

    /** 用户 ID */
    @Schema(description = "用户ID")
    private String id;

    /** 用户名 */
    @Schema(description = "用户名")
    private String username;

    /** 姓名 */
    @Schema(description = "姓名")
    private String name;

    /** 手机号 */
    @Schema(description = "手机号")
    private String phone;

    /** 邮箱 */
    @Schema(description = "邮箱")
    private String email;

    /** 头像地址 */
    @Schema(description = "头像地址")
    private String avatar;

    /** 角色名称列表（逗号分隔） */
    @Schema(description = "角色名称")
    private String roleName;

    /** 所属部门名称 */
    @Schema(description = "所属部门名称")
    private String deptName;

    /** 创建时间（时间戳） */
    @Schema(description = "创建时间")
    private Long createTime;
}
