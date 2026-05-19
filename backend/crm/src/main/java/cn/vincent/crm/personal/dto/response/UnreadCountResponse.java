package cn.vincent.crm.personal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 未读消息数量响应 DTO
 */
@Data
@Schema(description = "未读消息数量响应")
public class UnreadCountResponse {

    /** 未读消息数量 */
    @Schema(description = "未读消息数量")
    private long count;

    /**
     * 构建未读数量响应
     *
     * @param count 未读数量
     * @return 响应对象
     */
    public static UnreadCountResponse of(long count) {
        UnreadCountResponse response = new UnreadCountResponse();
        response.setCount(count);
        return response;
    }
}
