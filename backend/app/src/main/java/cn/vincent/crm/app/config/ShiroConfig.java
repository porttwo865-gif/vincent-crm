package cn.vincent.crm.app.config;

import cn.vincent.security.LocalRealm;
import cn.vincent.security.PermissionCache;
import cn.vincent.security.UserAuthService;
import cn.vincent.security.filter.AuthFilter;
import jakarta.servlet.Filter;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.redisson.api.RedissonClient;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro 安全配置 - 配置过滤器链、SecurityManager、Session 管理等
 */
@Configuration
public class ShiroConfig {

    /**
     * 认证过滤器 Bean（禁止 Spring 自动注册，由 Shiro 管理）
     */
    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }

    /**
     * 禁止 Spring 自动注册 AuthFilter（避免过滤器执行两次）
     */
    @Bean
    public FilterRegistrationBean<AuthFilter> disableAuthFilterRegistration(AuthFilter authFilter) {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>(authFilter);
        registration.setEnabled(false);
        return registration;
    }

    /**
     * LocalRealm Bean（依赖 UserLoginService 和 PermissionCache）
     */
    @Bean
    @DependsOn({"userLoginService", "permissionCache"})
    public LocalRealm localRealm(UserAuthService userAuthService, PermissionCache permissionCache) {
        LocalRealm realm = new LocalRealm();
        // 通过 setter 注入依赖（LocalRealm 不由 Spring 直接管理 @Resource）
        realm.setUserAuthService(userAuthService);
        realm.setPermissionCache(permissionCache);
        return realm;
    }

    /**
     * 安全管理器
     */
    @Bean
    public DefaultWebSecurityManager securityManager(Realm localRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(localRealm);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    /**
     * Session 管理器 - 使用 Redis 存储 Session
     */
    @Bean
    public SessionManager sessionManager(RedissonClient redissonClient) {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        // Session 超时时间：30 分钟
        sessionManager.setGlobalSessionTimeout(1800000L);
        // 定时检查过期 Session
        sessionManager.setSessionValidationSchedulerEnabled(true);
        // 删除过期的 Session
        sessionManager.setDeleteInvalidSessions(true);
        return sessionManager;
    }

    /**
     * Shiro 过滤器工厂 - 配置过滤器链
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager, AuthFilter authFilter) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);

        // 注册自定义过滤器
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("authc", authFilter);
        factoryBean.setFilters(filters);

        // 过滤器链定义（有序 LinkedHashMap）
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // RSA 公钥接口 - 匿名访问
        filterChainDefinitionMap.put("/rsa/key", "anon");
        // 登录接口 - 匿名访问
        filterChainDefinitionMap.put("/login", "anon");
        // 登录状态检测 - 匿名访问
        filterChainDefinitionMap.put("/is-login", "anon");
        // Swagger 文档 - 匿名访问
        filterChainDefinitionMap.put("/swagger-ui/**", "anon");
        filterChainDefinitionMap.put("/v3/api-docs/**", "anon");
        // 其他所有请求 - 需要认证
        filterChainDefinitionMap.put("/**", "authc");
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return factoryBean;
    }

    /**
     * 开启 Shiro 注解支持（@RequiresPermissions 等）
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
