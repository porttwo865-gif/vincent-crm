package cn.vincent.crm.contract.dto.response;

import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 合同列表响应
 */
@Data
@Schema(description = "合同列表响应")
public class ContractListResponse {

    /** 合同 ID */
    @Schema(description = "合同ID")
    private String id;

    /** 合同名称 */
    @Schema(description = "合同名称")
    private String name;

    /** 关联客户 ID */
    @Schema(description = "关联客户ID")
    private String customerId;

    /** 关联商机 ID */
    @Schema(description = "关联商机ID")
    private String opportunityId;

    /** 负责人 ID */
    @Schema(description = "负责人ID")
    private String owner;

    /** 负责人姓名 */
    @Schema(description = "负责人姓名")
    private String ownerName;

    /** 合同金额 */
    @Schema(description = "合同金额")
    private BigDecimal amount;

    /** 开始日期 */
    @Schema(description = "开始日期")
    private Long startDate;

    /** 结束日期 */
    @Schema(description = "结束日期")
    private Long endDate;

    /** 签约日期 */
    @Schema(description = "签约日期")
    private Long signedDate;

    /** 状态 */
    @Schema(description = "状态")
    private String status;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

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
