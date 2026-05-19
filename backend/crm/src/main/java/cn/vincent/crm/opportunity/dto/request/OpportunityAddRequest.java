package cn.vincent.crm.opportunity.dto.request;

import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 新增商机请求 DTO
 */
@Data
@Schema(description = "新增商机请求")
public class OpportunityAddRequest {

    /** 商机名称 */
    @NotBlank(message = "商机名称不能为空")
    @Schema(description = "商机名称")
    private String name;

    /** 关联客户 ID */
    @Schema(description = "关联客户ID")
    private String customerId;

    /** 关联联系人 ID */
    @Schema(description = "关联联系人ID")
    private String contactId;

    /** 负责人 ID */
    @Schema(description = "负责人ID")
    private String owner;

    /** 阶段 ID */
    @Schema(description = "阶段ID")
    private String stage;

    /** 预计金额 */
    @Schema(description = "预计金额")
    private BigDecimal amount;

    /** 预计成交时间 */
    @Schema(description = "预计成交时间")
    private Long expectedCloseTime;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 自定义字段值列表 */
    @Schema(description = "自定义字段值列表")
    private List<ModuleFieldValueDTO> moduleFields;
}
