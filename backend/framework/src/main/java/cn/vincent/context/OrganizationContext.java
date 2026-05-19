package cn.vincent.context;

import cn.vincent.security.SessionUtils;
import cn.vincent.security.dto.SessionUser;

/**
 * 组织上下文 - 从 Shiro Session 中获取当前请求的组织 ID
 */
public class OrganizationContext {

    private OrganizationContext() {
        // 工具类禁止实例化
    }

    /**
     * 获取当前组织 ID
     *
     * @return 组织 ID，未登录返回 null
     */
    public static String getOrganizationId() {
        SessionUser user = SessionUtils.getSessionUser();
        return user != null ? user.getOrganizationId() : null;
    }
}
