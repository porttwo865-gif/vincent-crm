package cn.vincent.crm.opportunity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商机详情响应 DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "商机详情响应")
public class OpportunityGetResponse extends OpportunityListResponse {

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 最新跟进人 */
    @Schema(description = "最新跟进人")
    private String follower;

    /** 最新跟进人姓名 */
    @Schema(description = "最新跟进人姓名")
    private String followerName;

    /** 最新跟进时间 */
    @Schema(description = "最新跟进时间")
    private Long followTime;
}
