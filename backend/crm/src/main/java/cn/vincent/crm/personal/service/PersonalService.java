package cn.vincent.crm.personal.service;

import cn.vincent.common.exception.GenericException;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.personal.dto.request.PasswordChangeRequest;
import cn.vincent.crm.personal.dto.request.PersonalUpdateRequest;
import cn.vincent.crm.personal.dto.response.PersonalInfoResponse;
import cn.vincent.crm.system.domain.Department;
import cn.vincent.crm.system.domain.Role;
import cn.vincent.crm.system.domain.User;
import cn.vincent.crm.system.mapper.DepartmentMapper;
import cn.vincent.crm.system.mapper.ExtUserMapper;
import cn.vincent.crm.system.mapper.RoleMapper;
import cn.vincent.crm.system.mapper.UserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 个人中心服务 - 处理个人信息查看与修改
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class PersonalService {

    /** 用户通用 Mapper */
    @Resource
    private UserMapper userMapper;

    /** 用户自定义 Mapper */
    @Resource
    private ExtUserMapper extUserMapper;

    /** 部门 Mapper */
    @Resource
    private DepartmentMapper departmentMapper;

    /** 角色 Mapper */
    @Resource
    private RoleMapper roleMapper;

    /**
     * 获取当前用户个人信息
     *
     * @param userId 当前用户 ID
     * @return 个人信息响应
     */
    public PersonalInfoResponse getPersonalInfo(String userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new GenericException(Translator.get("user.not.exist"));
        }

        PersonalInfoResponse response = BeanUtils.copyBean(new PersonalInfoResponse(), user);

        // 查询所属部门名称
        String deptId = extUserMapper.selectDeptIdByUserId(userId);
        if (StringUtils.isNotBlank(deptId)) {
            Department dept = departmentMapper.selectByPrimaryKey(deptId);
            if (dept != null) {
                response.setDeptName(dept.getName());
            }
        }

        // 查询角色名称（多角色以逗号拼接）
        List<String> roleIds = extUserMapper.selectRoleIdsByUserId(userId);
        if (roleIds != null && !roleIds.isEmpty()) {
            List<Role> roles = roleMapper.selectByIds(roleIds);
            String roleName = roles.stream()
                    .map(Role::getName)
                    .collect(Collectors.joining(", "));
            response.setRoleName(roleName);
        }

        return response;
    }

    /**
     * 修改当前用户个人信息（昵称、手机号、邮箱、头像）
     *
     * @param request 修改请求
     * @param userId  当前用户 ID
     */
    public void updatePersonalInfo(PersonalUpdateRequest request, String userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new GenericException(Translator.get("user.not.exist"));
        }

        // 按需更新各字段
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        user.setUpdateUser(userId);
        user.setUpdateTime(System.currentTimeMillis());
        userMapper.update(user);
    }

    /**
     * 修改当前用户密码
     *
     * @param request 修改密码请求
     * @param userId  当前用户 ID
     */
    public void changePassword(PasswordChangeRequest request, String userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new GenericException(Translator.get("user.not.exist"));
        }

        // 验证旧密码
        if (!BCrypt.checkpw(request.getOldPassword(), user.getPassword())) {
            throw new GenericException(Translator.get("old.password.incorrect"));
        }

        // 加密并更新新密码
        user.setPassword(BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt()));
        user.setUpdateUser(userId);
        user.setUpdateTime(System.currentTimeMillis());
        userMapper.update(user);
    }
}
