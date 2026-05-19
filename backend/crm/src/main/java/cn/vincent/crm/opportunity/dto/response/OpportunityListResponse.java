package cn.vincent.crm.opportunity.dto.response;

import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商机列表响应 DTO
 */
@Data
@Schema(description = "商机列表响应")
public class OpportunityListResponse {

    /** 商机 ID */
    @Schema(description = "商机ID")
    private String id;

    /** 商机名称 */
    @Schema(description = "商机名称")
    private String name;

    /** 关联客户 ID */
    @Schema(description = "关联客户ID")
    private String customerId;

    /** 客户名称 */
    @Schema(description = "客户名称")
    private String customerName;

    /** 关联联系人 ID */
    @Schema(description = "关联联系人ID")
    private String contactId;

    /** 联系人名称 */
    @Schema(description = "联系人名称")
    private String contactName;

    /** 负责人 ID */
    @Schema(description = "负责人ID")
    private String owner;

    /** 负责人姓名 */
    @Schema(description = "负责人姓名")
    private String ownerName;

    /** 阶段 ID */
    @Schema(description = "阶段ID")
    private String stage;

    /** 预计金额 */
    @Schema(description = "预计金额")
    private BigDecimal amount;

    /** 预计成交时间 */
    @Schema(description = "预计成交时间")
    private Long expectedCloseTime;

    /** 看板排序位置 */
    @Schema(description = "看板排序位置")
    private Long pos;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Long createTime;

    /** 自定义字段值列表 */
    @Schema(description = "自定义字段值列表")
    private List<ModuleFieldValueDTO> moduleFields;
}
