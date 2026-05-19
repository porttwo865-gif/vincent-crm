package cn.vincent.crm.clue.dto.response;

import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 线索列表响应
 */
@Data
@Schema(description = "线索列表响应")
public class ClueListResponse {

    /** 线索 ID */
    @Schema(description = "线索ID")
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

    /** 阶段 ID */
    @Schema(description = "阶段ID")
    private String stage;

    /** 联系人名称 */
    @Schema(description = "联系人名称")
    private String contact;

    /** 联系人电话 */
    @Schema(description = "联系人电话")
    private String phone;

    /** 意向产品 ID 列表 */
    @Schema(description = "意向产品ID列表")
    private List<String> products;

    /** 是否在线索池 */
    @Schema(description = "是否在线索池")
    private Boolean inSharedPool;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Long createTime;

    /** 部门 ID */
    @Schema(description = "部门ID")
    private String departmentId;

    /** 部门名称 */
    @Schema(description = "部门名称")
    private String departmentName;

    /** 最新跟进人 */
    @Schema(description = "最新跟进人")
    private String follower;

    /** 最新跟进人姓名 */
    @Schema(description = "最新跟进人姓名")
    private String followerName;

    /** 最新跟进时间 */
    @Schema(description = "最新跟进时间")
    private Long followTime;

    /** 转化类型 */
    @Schema(description = "转化类型")
    private String transitionType;

    /** 转化目标 ID */
    @Schema(description = "转化目标ID")
    private String transitionId;

    /** 自定义字段值 */
    @Schema(description = "自定义字段值")
    private List<ModuleFieldValueDTO> moduleFields;

    /** 创建人姓名 */
    @Schema(description = "创建人姓名")
    private String createUserName;

    /** 更新人姓名 */
    @Schema(description = "更新人姓名")
    private String updateUserName;
}
