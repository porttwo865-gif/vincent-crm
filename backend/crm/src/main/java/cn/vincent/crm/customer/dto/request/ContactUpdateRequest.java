package cn.vincent.crm.customer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 更新联系人请求 DTO
 */
@Data
@Schema(description = "更新联系人请求")
public class ContactUpdateRequest {

    /** 联系人 ID */
    @NotBlank(message = "联系人ID不能为空")
    @Schema(description = "联系人ID")
    private String id;

    /** 联系人姓名 */
    @Schema(description = "联系人姓名")
    private String name;

    /** 手机号 */
    @Schema(description = "手机号")
    private String phone;

    /** 邮箱 */
    @Schema(description = "邮箱")
    private String email;

    /** 职位 */
    @Schema(description = "职位")
    private String position;

    /** 部门 */
    @Schema(description = "部门")
    private String department;

    /** 是否主要联系人 */
    @Schema(description = "是否主要联系人")
    private Boolean isPrimary;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
