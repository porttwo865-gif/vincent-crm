package cn.vincent.crm.workbench.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 全局搜索客户结果项
 */
@Data
@Schema(description = "全局搜索客户结果项")
public class GlobalSearchCustomerItem {

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
}
