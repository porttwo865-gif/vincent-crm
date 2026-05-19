package cn.vincent.crm.system.service;

import cn.vincent.common.exception.GenericException;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.system.domain.*;
import cn.vincent.crm.system.dto.request.UserAddRequest;
import cn.vincent.crm.system.dto.request.UserPageRequest;
import cn.vincent.crm.system.dto.request.UserUpdateRequest;
import cn.vincent.crm.system.dto.response.UserListResponse;
import cn.vincent.crm.system.mapper.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户管理服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class UserService {

    /** 用户通用 Mapper */
    @Resource
    private UserMapper userMapper;

    /** 用户自定义 Mapper */
    @Resource
    private ExtUserMapper extUserMapper;

    /** 用户角色关联 Mapper */
    @Resource
    private UserRoleMapper userRoleMapper;

    /** 组织用户关联 Mapper */
    @Resource
    private OrganizationUserMapper organizationUserMapper;

    /** 部门 Mapper */
    @Resource
    private DepartmentMapper departmentMapper;

    /** 角色 Mapper */
    @Resource
    private RoleMapper roleMapper;

    /**
     * 新增用户
     *
     * @param request 新增请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 新增的用户实体
     */
    public User add(UserAddRequest request, String userId, String orgId) {
        // 校验用户名是否重复
        User existUser = extUserMapper.selectByUsernameAndOrgId(request.getUsername(), orgId);
        if (existUser != null) {
            throw new GenericException(Translator.get("user.username.exist"));
        }

        User user = new User();
        user.setId(IDGenerator.nextStr());
        user.setUsername(request.getUsername());
        // BCrypt 加密密码
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setEnable(true);
        user.setOrganizationId(orgId);
        user.setCreateUser(userId);
        user.setUpdateUser(userId);
        user.setCreateTime(System.currentTimeMillis());
        user.setUpdateTime(System.currentTimeMillis());
        userMapper.insert(user);

        // 创建组织用户关联
        OrganizationUser orgUser = new OrganizationUser();
        orgUser.setId(IDGenerator.nextStr());
        orgUser.setUserId(user.getId());
        orgUser.setOrganizationId(orgId);
        orgUser.setDepartmentId(request.getDepartmentId());
        organizationUserMapper.insert(orgUser);

        // 分配角色
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            List<UserRole> userRoles = new ArrayList<>();
            for (String roleId : request.getRoleIds()) {
                UserRole userRole = new UserRole();
                userRole.setId(IDGenerator.nextStr());
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                userRole.setOrganizationId(orgId);
                userRoles.add(userRole);
            }
            userRoleMapper.batchInsert(userRoles);
        }

        return user;
    }

    /**
     * 更新用户
     *
     * @param request 更新请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 更新后的用户实体
     */
    public User update(UserUpdateRequest request, String userId, String orgId) {
        User user = userMapper.selectByPrimaryKey(request.getId());
        if (user == null) {
            throw new GenericException(Translator.get("user.not.exist"));
        }

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        user.setUpdateUser(userId);
        user.setUpdateTime(System.currentTimeMillis());
        userMapper.update(user);

        // 更新部门关联
        if (request.getDepartmentId() != null) {
            String deptId = extUserMapper.selectDeptIdByUserId(user.getId());
            if (deptId != null) {
                // 更新已有记录的部门 ID
                extUserMapper.deleteOrgUserByUserId(user.getId());
            }
            OrganizationUser orgUser = new OrganizationUser();
            orgUser.setId(IDGenerator.nextStr());
            orgUser.setUserId(user.getId());
            orgUser.setOrganizationId(orgId);
            orgUser.setDepartmentId(request.getDepartmentId());
            organizationUserMapper.insert(orgUser);
        }

        // 更新角色关联
        if (request.getRoleIds() != null) {
            // 先删除旧关联
            extUserMapper.deleteUserRoleByUserId(user.getId());
            // 再新增新关联
            if (!request.getRoleIds().isEmpty()) {
                List<UserRole> userRoles = new ArrayList<>();
                for (String roleId : request.getRoleIds()) {
                    UserRole userRole = new UserRole();
                    userRole.setId(IDGenerator.nextStr());
                    userRole.setUserId(user.getId());
                    userRole.setRoleId(roleId);
                    userRole.setOrganizationId(orgId);
                    userRoles.add(userRole);
                }
                userRoleMapper.batchInsert(userRoles);
            }
        }

        return user;
    }

    /**
     * 删除用户
     *
     * @param id 用户 ID
     */
    public void delete(String id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            throw new GenericException(Translator.get("user.not.exist"));
        }

        // 删除用户角色关联
        extUserMapper.deleteUserRoleByUserId(id);
        // 删除组织用户关联
        extUserMapper.deleteOrgUserByUserId(id);
        // 删除用户
        userMapper.deleteByIds(List.of(id));
    }

    /**
     * 分页查询用户列表
     *
     * @param request 分页请求
     * @param orgId   组织 ID
     * @return 分页结果
     */
    public PagerWithOption<List<UserListResponse>> page(UserPageRequest request, String orgId) {
        PageHelper.startPage(request.getCurrent(), request.getPageSize());
        List<User> users = extUserMapper.selectUserPage(orgId, request.getKeyword(),
                request.getDepartmentId(), request.getEnable());
        PageInfo<User> pageInfo = new PageInfo<>(users);

        // 转换为响应 DTO
        List<UserListResponse> responseList = users.stream()
                .map(user -> {
                    UserListResponse response = BeanUtils.copyBean(new UserListResponse(), user);

                    // 查询部门信息
                    String deptId = extUserMapper.selectDeptIdByUserId(user.getId());
                    if (StringUtils.isNotBlank(deptId)) {
                        response.setDepartmentId(deptId);
                        Department dept = departmentMapper.selectByPrimaryKey(deptId);
                        if (dept != null) {
                            response.setDepartmentName(dept.getName());
                        }
                    }

                    // 查询角色名称列表
                    List<String> roleIds = extUserMapper.selectRoleIdsByUserId(user.getId());
                    if (roleIds != null && !roleIds.isEmpty()) {
                        List<Role> roles = roleMapper.selectByIds(roleIds);
                        response.setRoleNames(roles.stream().map(Role::getName).toList());
                    } else {
                        response.setRoleNames(Collections.emptyList());
                    }

                    return response;
                })
                .toList();

        return PagerWithOption.of(responseList, pageInfo.getTotal(), request.getCurrent(), request.getPageSize());
    }

    /**
     * 启用用户
     *
     * @param id 用户 ID
     */
    public void enable(String id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            throw new GenericException(Translator.get("user.not.exist"));
        }
        user.setEnable(true);
        user.setUpdateTime(System.currentTimeMillis());
        userMapper.update(user);
    }

    /**
     * 禁用用户（踢出 Session）
     *
     * @param id 用户 ID
     */
    public void disable(String id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            throw new GenericException(Translator.get("user.not.exist"));
        }
        user.setEnable(false);
        user.setUpdateTime(System.currentTimeMillis());
        userMapper.update(user);

        // 踢出用户会话
        cn.vincent.security.SessionUtils.kickOutUser(id);
    }

    /**
     * 重置密码
     *
     * @param id          用户 ID
     * @param newPassword 新密码
     */
    public void resetPassword(String id, String newPassword) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            throw new GenericException(Translator.get("user.not.exist"));
        }
        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        user.setUpdateTime(System.currentTimeMillis());
        userMapper.update(user);

        // 踢出用户会话，强制重新登录
        cn.vincent.security.SessionUtils.kickOutUser(id);
    }
}
