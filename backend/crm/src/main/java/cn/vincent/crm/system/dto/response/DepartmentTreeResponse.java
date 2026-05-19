package cn.vincent.crm.system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 部门树形响应 DTO
 */
@Data
@Schema(description = "部门树形响应")
public class DepartmentTreeResponse {

    /** 部门 ID */
    @Schema(description = "部门ID")
    private String id;

    /** 部门名称 */
    @Schema(description = "部门名称")
    private String name;

    /** 父部门 ID */
    @Schema(description = "父部门ID")
    private String parentId;

    /** 排序 */
    @Schema(description = "排序")
    private Integer sort;

    /** 子部门列表 */
    @Schema(description = "子部门列表")
    private List<DepartmentTreeResponse> children;
}
