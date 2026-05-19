package cn.vincent.crm.workbench.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 全局搜索线索结果项
 */
@Data
@Schema(description = "全局搜索线索结果项")
public class GlobalSearchClueItem {

    /** 线索 ID */
    @Schema(description = "线索ID")
    private String id;

    /** 客户名称 */
    @Schema(description = "客户名称")
    private String name;

    /** 联系人电话 */
    @Schema(description = "联系人电话")
    private String phone;

    /** 负责人 ID */
    @Schema(description = "负责人ID")
    private String owner;

    /** 负责人姓名 */
    @Schema(description = "负责人姓名")
    private String ownerName;
}
