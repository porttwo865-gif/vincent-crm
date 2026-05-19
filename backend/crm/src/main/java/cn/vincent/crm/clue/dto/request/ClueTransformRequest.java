package cn.vincent.crm.clue.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 线索转客户请求
 */
@Data
@Schema(description = "线索转客户请求")
public class ClueTransformRequest {

    /** 线索 ID */
    @NotBlank(message = "线索ID不能为空")
    @Schema(description = "线索ID")
    private String clueId;

    /** 转化模式（NEW 新建客户 / LINK 关联已有客户） */
    @NotBlank(message = "转化模式不能为空")
    @Schema(description = "转化模式（NEW/LINK）")
    private String mode;

    /** 新建客户时的数据 */
    @Schema(description = "新建客户时的数据")
    private Object customerData;

    /** 关联已有客户时的客户 ID */
    @Schema(description = "关联已有客户时的客户ID")
    private String customerId;
}
