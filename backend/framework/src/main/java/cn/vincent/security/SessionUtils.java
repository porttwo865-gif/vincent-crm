package cn.vincent.security;

import cn.vincent.security.dto.SessionUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import java.util.Collection;
import java.util.Objects;

/**
 * 会话工具类 - 基于 Shiro Session 获取当前登录用户信息
 */
public class SessionUtils {

    private SessionUtils() {
        // 工具类禁止实例化
    }

    /**
     * 获取当前用户 ID
     *
     * @return 用户 ID，未登录返回 null
     */
    public static String getUserId() {
        SessionUser user = getSessionUser();
        return user != null ? user.getUserId() : null;
    }

    /**
     * 获取当前组织 ID
     *
     * @return 组织 ID，未登录返回 null
     */
    public static String getOrganizationId() {
        SessionUser user = getSessionUser();
        return user != null ? user.getOrganizationId() : null;
    }

    /**
     * 获取当前会话用户信息
     *
     * @return SessionUser 对象，未登录返回 null
     */
    public static SessionUser getSessionUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            return (SessionUser) subject.getPrincipal();
        }
        return null;
    }

    /**
     * 根据 userId 踢出用户会话（使其失效）
     *
     * @param userId 用户 ID
     */
    public static void kickOutUser(String userId) {
        try {
            org.apache.shiro.mgt.SecurityManager securityManager = SecurityUtils.getSecurityManager();
            if (securityManager instanceof DefaultSessionManager sessionManager) {
                Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();
                for (Session session : sessions) {
                    Object principal = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                    if (principal instanceof org.apache.shiro.subject.SimplePrincipalCollection principals) {
                        Object primary = principals.getPrimaryPrincipal();
                        if (primary instanceof SessionUser sessionUser
                                && Objects.equals(sessionUser.getUserId(), userId)) {
                            sessionManager.getSessionDAO().delete(session);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 踢出用户失败不应影响业务流程，仅记录日志
            org.slf4j.LoggerFactory.getLogger(SessionUtils.class)
                    .error("踢出用户会话失败, userId: {}", userId, e);
        }
    }
}
