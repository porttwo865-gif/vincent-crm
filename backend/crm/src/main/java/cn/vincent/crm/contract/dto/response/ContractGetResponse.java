package cn.vincent.crm.contract.dto.response;

import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 合同详情响应
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "合同详情响应")
public class ContractGetResponse extends ContractListResponse {

    /** 更新人姓名 */
    @Schema(description = "更新人姓名")
    private String updateUserName;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private Long updateTime;
}
