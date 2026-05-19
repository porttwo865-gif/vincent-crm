package cn.vincent.crm.clue.dto.request;

import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 新增线索请求
 */
@Data
@Schema(description = "新增线索请求")
public class ClueAddRequest {

    /** 客户名称 */
    @NotBlank(message = "客户名称不能为空")
    @Schema(description = "客户名称")
    private String name;

    /** 负责人 ID */
    @Schema(description = "负责人ID")
    private String owner;

    /** 联系人名称 */
    @Schema(description = "联系人名称")
    private String contact;

    /** 联系人电话 */
    @Schema(description = "联系人电话")
    private String phone;

    /** 意向产品 ID 列表 */
    @Schema(description = "意向产品ID列表")
    private List<String> products;

    /** 自定义字段值 */
    @Schema(description = "自定义字段值")
    private List<ModuleFieldValueDTO> moduleFields;
}
