package cn.vincent.crm.follow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 添加跟进记录请求
 */
@Data
@Schema(description = "添加跟进记录请求")
public class FollowRecordAddRequest {

    /** 业务类型：CLUE/CUSTOMER/OPPORTUNITY/CONTRACT */
    @NotBlank(message = "业务类型不能为空")
    @Schema(description = "业务类型：CLUE/CUSTOMER/OPPORTUNITY/CONTRACT")
    private String bizType;

    /** 业务对象 ID */
    @NotBlank(message = "业务对象ID不能为空")
    @Schema(description = "业务对象ID")
    private String bizId;

    /** 跟进内容 */
    @NotBlank(message = "跟进内容不能为空")
    @Schema(description = "跟进内容")
    private String content;

    /** 跟进方式：phone/visit/email/wechat/other */
    @Schema(description = "跟进方式：phone/visit/email/wechat/other")
    private String followType;

    /** 下次跟进时间 */
    @Schema(description = "下次跟进时间")
    private Long nextFollowTime;

    /** 附件（JSON 字符串） */
    @Schema(description = "附件（JSON字符串）")
    private String attachments;
}
