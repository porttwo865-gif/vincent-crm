package cn.vincent.security.dto;

import lombok.Data;

import java.util.List;

/**
 * 部门数据权限 DTO - 描述用户可见的数据范围
 */
@Data
public class DeptDataPermissionDTO {

    /** 是否可查看全部数据 */
    private boolean all;

    /** 可见部门 ID 列表 */
    private List<String> deptIds;

    /** 可见用户 ID 列表（SELF 模式下仅包含自身） */
    private List<String> userIds;
}
