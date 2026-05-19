package cn.vincent.crm.customer.dto.response;

import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 客户列表响应 DTO
 */
@Data
@Schema(description = "客户列表响应")
public class CustomerListResponse {

    /** 客户 ID */
    @Schema(description = "客户ID")
    private String id;

    /** 客户名称 */
    @Schema(description = "客户名称")
    private String name;

    /** 负责人 ID */
    @Schema(description = "负责人ID")
    private String owner;

    /** 负责人姓名 */
    @Schema(description = "负责人姓名")
    private String ownerName;

    /** 是否在公海池 */
    @Schema(description = "是否在公海池")
    private Boolean inSharedPool;

    /** 领取时间 */
    @Schema(description = "领取时间")
    private Long collectionTime;

    /** 最新跟进人 */
    @Schema(description = "最新跟进人")
    private String follower;

    /** 最新跟进人姓名 */
    @Schema(description = "最新跟进人姓名")
    private String followerName;

    /** 最新跟进时间 */
    @Schema(description = "最新跟进时间")
    private Long followTime;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Long createTime;

    /** 创建人姓名 */
    @Schema(description = "创建人姓名")
    private String createUserName;

    /** 自定义字段值列表 */
    @Schema(description = "自定义字段值列表")
    private List<ModuleFieldValueDTO> moduleFields;
}
