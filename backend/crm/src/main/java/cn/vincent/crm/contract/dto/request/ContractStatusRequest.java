package cn.vincent.crm.contract.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 合同状态变更请求
 */
@Data
@Schema(description = "合同状态变更请求")
public class ContractStatusRequest {

    /** 合同 ID */
    @NotBlank(message = "合同ID不能为空")
    @Schema(description = "合同ID")
    private String id;

    /** 状态 */
    @NotBlank(message = "状态不能为空")
    @Schema(description = "状态")
    private String status;
}
