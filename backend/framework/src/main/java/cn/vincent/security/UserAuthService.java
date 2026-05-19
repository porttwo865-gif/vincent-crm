package cn.vincent.security;

import cn.vincent.security.dto.SessionUser;

/**
 * 用户认证服务接口 - 由业务模块实现
 * <p>
 * 框架模块通过此接口解耦对业务模块的依赖
 */
public interface UserAuthService {

    /**
     * 根据用户名认证用户，返回会话用户信息
     *
     * @param username 用户名
     * @return SessionUser 对象，用户不存在返回 null
     */
    SessionUser authenticateUser(String username);
}
