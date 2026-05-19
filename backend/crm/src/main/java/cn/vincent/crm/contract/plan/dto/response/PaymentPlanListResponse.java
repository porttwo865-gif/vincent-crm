package cn.vincent.crm.contract.plan.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 回款计划列表响应
 */
@Data
@Schema(description = "回款计划列表响应")
public class PaymentPlanListResponse {

    /** 回款计划 ID */
    @Schema(description = "回款计划ID")
    private String id;

    /** 关联合同 ID */
    @Schema(description = "关联合同ID")
    private String contractId;

    /** 期数 */
    @Schema(description = "期数")
    private Integer planNum;

    /** 计划金额 */
    @Schema(description = "计划金额")
    private BigDecimal amount;

    /** 预计回款日期 */
    @Schema(description = "预计回款日期")
    private Long expectedDate;

    /** 实际回款日期 */
    @Schema(description = "实际回款日期")
    private Long actualDate;

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
}
