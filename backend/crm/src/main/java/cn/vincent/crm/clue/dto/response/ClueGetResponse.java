package cn.vincent.crm.clue.dto.response;

import cn.vincent.crm.clue.domain.ClueOwner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 线索详情响应
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "线索详情响应")
public class ClueGetResponse extends ClueListResponse {

    /** 负责人变更历史 */
    @Schema(description = "负责人变更历史")
    private List<ClueOwner> ownerHistory;
}
