package cn.vincent.crm.system.mapper;

import cn.vincent.crm.system.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtUserMapper {

    /**
     * 根据用户名和组织 ID 查询用户
     *
     * @param username      用户名
     * @param organizationId 组织 ID
     * @return 用户实体
     */
    User selectByUsernameAndOrgId(@Param("username") String username, @Param("organizationId") String organizationId);

    /**
     * 根据用户 ID 查询角色 ID 列表
     *
     * @param userId 用户 ID
     * @return 角色 ID 列表
     */
    List<String> selectRoleIdsByUserId(@Param("userId") String userId);

    /**
     * 根据角色 ID 列表查询权限标识列表
     *
     * @param roleIds 角色 ID 列表
     * @return 权限标识列表
     */
    List<String> selectPermissionIdsByRoleIds(@Param("roleIds") List<String> roleIds);

    /**
     * 根据条件分页查询用户列表
     *
     * @param organizationId 组织 ID
     * @param keyword        搜索关键词（用户名/姓名/邮箱/手机号）
     * @param departmentId   部门 ID
     * @param enable         是否启用
     * @return 用户列表
     */
    List<User> selectUserPage(@Param("organizationId") String organizationId,
                              @Param("keyword") String keyword,
                              @Param("departmentId") String departmentId,
                              @Param("enable") Boolean enable);

    /**
     * 删除用户角色关联（按用户 ID）
     *
     * @param userId 用户 ID
     * @return 影响行数
     */
    int deleteUserRoleByUserId(@Param("userId") String userId);

    /**
     * 删除组织用户关联（按用户 ID）
     *
     * @param userId 用户 ID
     * @return 影响行数
     */
    int deleteOrgUserByUserId(@Param("userId") String userId);

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

    /**
     * 查询用户所属部门 ID
     *
     * @param userId 用户 ID
     * @return 部门 ID
     */
    String selectDeptIdByUserId(@Param("userId") String userId);
}