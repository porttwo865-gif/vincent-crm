package cn.vincent.crm.follow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 更新跟进记录请求
 */
@Data
@Schema(description = "更新跟进记录请求")
public class FollowRecordUpdateRequest {

    /** 跟进记录 ID */
    @NotBlank(message = "跟进记录ID不能为空")
    @Schema(description = "跟进记录ID")
    private String id;

    /** 跟进内容 */
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
