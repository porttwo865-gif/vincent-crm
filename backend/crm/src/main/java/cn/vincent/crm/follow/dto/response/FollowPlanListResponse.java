package cn.vincent.crm.follow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 跟进计划列表响应
 */
@Data
@Schema(description = "跟进计划列表响应")
public class FollowPlanListResponse {

    /** 跟进计划 ID */
    @Schema(description = "跟进计划ID")
    private String id;

    /** 业务类型 */
    @Schema(description = "业务类型：CLUE/CUSTOMER/OPPORTUNITY/CONTRACT")
    private String bizType;

    /** 业务对象 ID */
    @Schema(description = "业务对象ID")
    private String bizId;

    /** 计划跟进时间 */
    @Schema(description = "计划跟进时间")
    private Long planTime;

    /** 计划内容 */
    @Schema(description = "计划内容")
    private String content;

    /** 提前提醒分钟数 */
    @Schema(description = "提前提醒分钟数")
    private Integer remindBefore;

    /** 状态：pending/done/expired */
    @Schema(description = "状态：pending/done/expired")
    private String status;

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
