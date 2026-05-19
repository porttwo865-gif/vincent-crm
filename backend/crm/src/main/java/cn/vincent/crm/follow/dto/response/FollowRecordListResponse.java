package cn.vincent.crm.follow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 跟进记录列表响应
 */
@Data
@Schema(description = "跟进记录列表响应")
public class FollowRecordListResponse {

    /** 跟进记录 ID */
    @Schema(description = "跟进记录ID")
    private String id;

    /** 业务类型 */
    @Schema(description = "业务类型：CLUE/CUSTOMER/OPPORTUNITY/CONTRACT")
    private String bizType;

    /** 业务对象 ID */
    @Schema(description = "业务对象ID")
    private String bizId;

    /** 跟进内容 */
    @Schema(description = "跟进内容")
    private String content;

    /** 跟进方式 */
    @Schema(description = "跟进方式：phone/visit/email/wechat/other")
    private String followType;

    /** 下次跟进时间 */
    @Schema(description = "下次跟进时间")
    private Long nextFollowTime;

    /** 附件（JSON 字符串） */
    @Schema(description = "附件（JSON字符串）")
    private String attachments;

    /** 负责人 ID */
    @Schema(description = "负责人ID")
    private String owner;

    /** 负责人姓名 */
    @Schema(description = "负责人姓名")
    private String ownerName;

    /** 创建人 */
    @Schema(description = "创建人ID")
    private String createUser;

    /** 创建人姓名 */
    @Schema(description = "创建人姓名")
    private String createUserName;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Long createTime;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private Long updateTime;
}
