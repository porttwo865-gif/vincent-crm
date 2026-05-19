package cn.vincent.crm.workbench.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 工作台待办事项响应 DTO
 */
@Data
@Schema(description = "工作台待办事项响应")
public class WorkbenchTodoResponse {

    /** 待跟进计划数量 */
    @Schema(description = "待跟进计划数量")
    private Long pendingFollowPlans;

    /** 待我审批数量 */
    @Schema(description = "待我审批数量")
    private Long pendingApprovals;

    /** 即将到期合同数量（30天内） */
    @Schema(description = "即将到期合同数量（30天内）")
    private Long expiringContracts;

    /** 逾期回款计划数量 */
    @Schema(description = "逾期回款计划数量")
    private Long overduePaymentPlans;
}
