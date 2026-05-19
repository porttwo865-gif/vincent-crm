package cn.vincent.crm.customer.dto.response;

import cn.vincent.crm.customer.domain.CustomerOwner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 客户详情响应 DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "客户详情响应")
public class CustomerGetResponse extends CustomerListResponse {

    /** 联系人列表 */
    @Schema(description = "联系人列表")
    private List<ContactListResponse> contacts;

    /** 负责人变更历史 */
    @Schema(description = "负责人变更历史")
    private List<CustomerOwner> ownerHistory;
}
