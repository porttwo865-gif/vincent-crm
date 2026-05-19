package cn.vincent.crm.system.service;

import cn.vincent.common.exception.GenericException;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.system.domain.Role;
import cn.vincent.crm.system.domain.RolePermission;
import cn.vincent.crm.system.domain.RoleScopeDept;
import cn.vincent.crm.system.dto.request.RoleAddRequest;
import cn.vincent.crm.system.dto.request.RoleUpdateRequest;
import cn.vincent.crm.system.dto.response.RoleListResponse;
import cn.vincent.crm.system.mapper.ExtRoleMapper;
import cn.vincent.crm.system.mapper.RoleMapper;
import cn.vincent.crm.system.mapper.RolePermissionMapper;
import cn.vincent.crm.system.mapper.RoleScopeDeptMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色管理服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class RoleService {

    /** 角色通用 Mapper */
    @Resource
    private RoleMapper roleMapper;

    /** 角色自定义 Mapper */
    @Resource
    private ExtRoleMapper extRoleMapper;

    /** 角色权限关联 Mapper */
    @Resource
    private RolePermissionMapper rolePermissionMapper;

    /** 角色数据范围-部门关联 Mapper */
    @Resource
    private RoleScopeDeptMapper roleScopeDeptMapper;

    /**
     * 新增角色 + 分配权限 + 关联部门范围
     *
     * @param request 新增请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 新增的角色实体
     */
    public Role add(RoleAddRequest request, String userId, String orgId) {
        Role role = new Role();
        role.setId(IDGenerator.nextStr());
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setDataScope(request.getDataScope() != null ? request.getDataScope() : "SELF");
        role.setEnable(true);
        role.setOrganizationId(orgId);
        role.setCreateUser(userId);
        role.setUpdateUser(userId);
        role.setCreateTime(System.currentTimeMillis());
        role.setUpdateTime(System.currentTimeMillis());
        roleMapper.insert(role);

        // 分配权限
        assignPermissions(role.getId(), request.getPermissionIds());

        // 关联部门范围（数据范围为 DEPT_CUSTOM 时）
        if ("DEPT_CUSTOM".equals(request.getDataScope()) && request.getDeptIds() != null) {
            assignDeptScope(role.getId(), request.getDeptIds());
        }

        return role;
    }

    /**
     * 更新角色 + 重新分配权限
     *
     * @param request 更新请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 更新后的角色实体
     */
    public Role update(RoleUpdateRequest request, String userId, String orgId) {
        Role role = roleMapper.selectByPrimaryKey(request.getId());
        if (role == null) {
            throw new GenericException(Translator.get("role.not.exist"));
        }

        if (request.getName() != null) {
            role.setName(request.getName());
        }
        if (request.getDescription() != null) {
            role.setDescription(request.getDescription());
        }
        if (request.getDataScope() != null) {
            role.setDataScope(request.getDataScope());
        }
        role.setUpdateUser(userId);
        role.setUpdateTime(System.currentTimeMillis());
        roleMapper.update(role);

        // 重新分配权限
        if (request.getPermissionIds() != null) {
            assignPermissions(role.getId(), request.getPermissionIds());
        }

        // 重新关联部门范围
        if (request.getDeptIds() != null) {
            assignDeptScope(role.getId(), request.getDeptIds());
        }

        return role;
    }

    /**
     * 删除角色（校验是否有关联用户）
     *
     * @param id 角色 ID
     */
    public void delete(String id) {
        Role role = roleMapper.selectByPrimaryKey(id);
        if (role == null) {
            throw new GenericException(Translator.get("role.not.exist"));
        }

        // 校验是否有关联用户
        int userCount = extRoleMapper.countUsersByRoleId(id);
        if (userCount > 0) {
            throw new GenericException(Translator.get("role.has.users"));
        }

        // 删除角色权限关联
        extRoleMapper.deleteRolePermissionByRoleId(id);
        // 删除角色数据范围-部门关联
        extRoleMapper.deleteRoleScopeDeptByRoleId(id);
        // 删除角色
        roleMapper.deleteByIds(List.of(id));
    }

    /**
     * 角色列表
     *
     * @param orgId 组织 ID
     * @return 角色列表响应
     */
    public List<RoleListResponse> list(String orgId) {
        List<Role> roles = extRoleMapper.selectByOrgId(orgId);
        if (roles == null || roles.isEmpty()) {
            return new ArrayList<>();
        }

        return roles.stream()
                .map(role -> {
                    RoleListResponse response = BeanUtils.copyBean(new RoleListResponse(), role);
                    // 查询关联用户数
                    response.setUserCount(extRoleMapper.countUsersByRoleId(role.getId()));
                    return response;
                })
                .toList();
    }

    /**
     * 获取角色权限列表
     *
     * @param roleId 角色 ID
     * @return 权限 ID 列表
     */
    public List<String> getPermissions(String roleId) {
        Role role = roleMapper.selectByPrimaryKey(roleId);
        if (role == null) {
            throw new GenericException(Translator.get("role.not.exist"));
        }
        return extRoleMapper.selectPermissionIdsByRoleId(roleId);
    }

    /**
     * 分配权限（先删后增）
     *
     * @param roleId        角色 ID
     * @param permissionIds 权限 ID 列表
     */
    private void assignPermissions(String roleId, List<String> permissionIds) {
        // 先删除旧关联
        extRoleMapper.deleteRolePermissionByRoleId(roleId);

        // 再新增新关联
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<RolePermission> rolePermissions = new ArrayList<>();
            for (String permissionId : permissionIds) {
                RolePermission rp = new RolePermission();
                rp.setId(IDGenerator.nextStr());
                rp.setRoleId(roleId);
                rp.setPermissionId(permissionId);
                rolePermissions.add(rp);
            }
            rolePermissionMapper.batchInsert(rolePermissions);
        }
    }

    /**
     * 关联部门范围（先删后增）
     *
     * @param roleId  角色 ID
     * @param deptIds 部门 ID 列表
     */
    private void assignDeptScope(String roleId, List<String> deptIds) {
        // 先删除旧关联
        extRoleMapper.deleteRoleScopeDeptByRoleId(roleId);

        // 再新增新关联
        if (deptIds != null && !deptIds.isEmpty()) {
            List<RoleScopeDept> scopeDepts = new ArrayList<>();
            for (String deptId : deptIds) {
                RoleScopeDept rsd = new RoleScopeDept();
                rsd.setId(IDGenerator.nextStr());
                rsd.setRoleId(roleId);
                rsd.setDeptId(deptId);
                scopeDepts.add(rsd);
            }
            roleScopeDeptMapper.batchInsert(scopeDepts);
        }
    }
}
