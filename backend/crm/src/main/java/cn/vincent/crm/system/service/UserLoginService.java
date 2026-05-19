package cn.vincent.crm.system.service;

import cn.vincent.common.exception.GenericException;
import cn.vincent.common.util.RsaUtils;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.system.domain.User;
import cn.vincent.crm.system.mapper.ExtUserMapper;
import cn.vincent.security.PermissionCache;
import cn.vincent.security.UserAuthService;
import cn.vincent.security.dto.SessionUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 用户登录服务 - 处理认证、密码校验、构建会话用户
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class UserLoginService implements UserAuthService {

    /** 用户自定义 Mapper */
    @Resource
    private ExtUserMapper extUserMapper;

    /** 权限缓存 */
    @Resource
    private PermissionCache permissionCache;

    /**
     * 认证用户（供 LocalRealm 调用，仅根据用户名获取用户信息）
     * <p>
     * 密码校验在 login 流程中完成，此处仅返回用户实体用于构建 SessionUser
     *
     * @param username 用户名
     * @return SessionUser 对象，用户不存在返回 null
     */
    @Override
    public SessionUser authenticateUser(String username) {
        // 默认组织 ID（后续支持多组织时从请求参数获取）
        String defaultOrgId = "org_default";

        User user = extUserMapper.selectByUsernameAndOrgId(username, defaultOrgId);
        if (user == null) {
            return null;
        }

        return buildSessionUser(user);
    }

    /**
     * 登录认证 - 校验用户名密码并返回 SessionUser
     *
     * @param username 用户名
     * @param password RSA 加密的密码
     * @param rsaKey   RSA 密钥标识
     * @param orgId    组织 ID
     * @return SessionUser 会话用户信息
     */
    public SessionUser login(String username, String password, String rsaKey, String orgId) {
        // 1. 查询用户
        User user = extUserMapper.selectByUsernameAndOrgId(username, orgId);
        if (user == null) {
            throw new GenericException(Translator.get("user.not.exist"));
        }

        // 2. RSA 解密密码
        String rawPassword;
        try {
            rawPassword = RsaUtils.decrypt(rsaKey, password);
        } catch (Exception e) {
            log.error("RSA 解密密码失败: {}", e.getMessage());
            throw new GenericException(Translator.get("user.password.error"));
        }

        // 3. BCrypt 校验密码
        if (!BCrypt.checkpw(rawPassword, user.getPassword())) {
            throw new GenericException(Translator.get("user.password.error"));
        }

        // 4. 检查用户是否启用
        if (user.getEnable() == null || !user.getEnable()) {
            throw new GenericException(Translator.get("user.disabled"));
        }

        // 5. 构建 SessionUser 返回
        return buildSessionUser(user);
    }

    /**
     * 根据 User 实体构建 SessionUser，并加载权限和角色
     *
     * @param user 用户实体
     * @return SessionUser 会话用户信息
     */
    private SessionUser buildSessionUser(User user) {
        SessionUser sessionUser = new SessionUser();
        sessionUser.setUserId(user.getId());
        sessionUser.setUsername(user.getUsername());
        sessionUser.setName(user.getName());
        sessionUser.setOrganizationId(user.getOrganizationId());

        // 查询角色 ID 列表
        List<String> roleIds = extUserMapper.selectRoleIdsByUserId(user.getId());
        sessionUser.setRoleIds(roleIds != null ? roleIds : Collections.emptyList());

        // 查询权限标识列表
        List<String> permissions;
        if (roleIds != null && !roleIds.isEmpty()) {
            permissions = extUserMapper.selectPermissionIdsByRoleIds(roleIds);
        } else {
            permissions = Collections.emptyList();
        }
        sessionUser.setPermissions(permissions);

        // 写入权限缓存
        permissionCache.setPermissionIds(user.getId(), user.getOrganizationId(), permissions);

        return sessionUser;
    }
}
