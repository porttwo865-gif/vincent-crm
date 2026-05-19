package cn.vincent.crm.system.mapper;

import cn.vincent.crm.system.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtRoleMapper {

    /**
     * 查询组织下所有角色
     *
     * @param orgId 组织 ID
     * @return 角色列表
     */
    List<Role> selectByOrgId(@Param("orgId") String orgId);

    /**
     * 查询角色权限 ID 列表
     *
     * @param roleId 角色 ID
     * @return 权限 ID 列表
     */
    List<String> selectPermissionIdsByRoleId(@Param("roleId") String roleId);

    /**
     * 统计角色关联用户数
     *
     * @param roleId 角色 ID
     * @return 关联用户数
     */
    int countUsersByRoleId(@Param("roleId") String roleId);

    /**
     * 删除角色权限关联（按角色 ID）
     *
     * @param roleId 角色 ID
     * @return 影响行数
     */
    int deleteRolePermissionByRoleId(@Param("roleId") String roleId);

    /**
     * 删除角色数据范围-部门关联（按角色 ID）
     *
     * @param roleId 角色 ID
     * @return 影响行数
     */
    int deleteRoleScopeDeptByRoleId(@Param("roleId") String roleId);
}
