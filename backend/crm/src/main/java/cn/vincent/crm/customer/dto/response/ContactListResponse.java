package cn.vincent.crm.customer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 联系人列表响应 DTO
 */
@Data
@Schema(description = "联系人列表响应")
public class ContactListResponse {

    /** 联系人 ID */
    @Schema(description = "联系人ID")
    private String id;

    /** 所属客户 ID */
    @Schema(description = "所属客户ID")
    private String customerId;

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

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Long createTime;
}
