package cn.vincent.security;

import cn.vincent.security.dto.SessionUser;
import jakarta.annotation.Resource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.List;

/**
 * 本地认证鉴权 Realm - 基于数据库查询进行用户认证和授权
 */
public class LocalRealm extends AuthorizingRealm {

    /** 用户认证服务（由业务模块实现） */
    private UserAuthService userAuthService;

    /** 权限缓存 */
    private PermissionCache permissionCache;

    /**
     * 设置用户认证服务（由 ShiroConfig 注入）
     *
     * @param userAuthService 用户认证服务
     */
    public void setUserAuthService(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    /**
     * 设置权限缓存（由 ShiroConfig 注入）
     *
     * @param permissionCache 权限缓存
     */
    public void setPermissionCache(PermissionCache permissionCache) {
        this.permissionCache = permissionCache;
    }

    /**
     * 认证 - 校验用户名密码
     *
     * @param token 认证令牌
     * @return 认证信息
     * @throws AuthenticationException 认证失败异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();

        // 通过 UserAuthService 认证（密码校验在 Service 中完成）
        SessionUser sessionUser = userAuthService.authenticateUser(username);
        if (sessionUser == null) {
            return null;
        }

        return new SimpleAuthenticationInfo(sessionUser, upToken.getPassword(), getName());
    }

    /**
     * 授权 - 加载用户角色和权限
     *
     * @param principals 已认证的主体
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SessionUser sessionUser = (SessionUser) principals.getPrimaryPrincipal();
        String userId = sessionUser.getUserId();
        String orgId = sessionUser.getOrganizationId();

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // 从缓存获取权限列表
        List<String> permissions = permissionCache.getPermissionIds(userId, orgId);
        info.addStringPermissions(permissions);

        // 添加角色 ID 作为角色标识
        if (sessionUser.getRoleIds() != null) {
            info.addRoles(sessionUser.getRoleIds());
        }

        return info;
    }
}
