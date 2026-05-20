package cn.vincent.crm.config;

import cn.vincent.security.LocalRealm;
import cn.vincent.security.PermissionCache;
import cn.vincent.security.UserAuthService;
import cn.vincent.security.filter.AuthFilter;
import jakarta.servlet.Filter;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 测试环境 Shiro 安全配置 - 与正式环境完全相同的配置，
 * 用于替代 app 模块中的 ShiroConfig（避免循环依赖）
 */
@Configuration
public class TestShiroConfig {

    /**
     * 认证过滤器 Bean
     */
    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }

    /**
     * 禁止 Spring 自动注册 AuthFilter
     */
    @Bean
    public FilterRegistrationBean<AuthFilter> disableAuthFilterRegistration(AuthFilter authFilter) {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>(authFilter);
        registration.setEnabled(false);
        return registration;
    }

    /**
     * LocalRealm Bean
     */
    @Bean
    @DependsOn({"userLoginService", "permissionCache"})
    public LocalRealm localRealm(UserAuthService userAuthService, PermissionCache permissionCache) {
        LocalRealm realm = new LocalRealm();
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
     * Session 管理器
     */
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(1800000L);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setDeleteInvalidSessions(true);

        SimpleCookie sessionIdCookie = new SimpleCookie();
        sessionIdCookie.setName("JSESSIONID");
        sessionIdCookie.setPath("/");
        sessionIdCookie.setHttpOnly(true);
        sessionIdCookie.setMaxAge(-1);
        sessionManager.setSessionIdCookie(sessionIdCookie);
        sessionManager.setSessionIdCookieEnabled(true);

        return sessionManager;
    }

    /**
     * Shiro 过滤器工厂
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager,
                                                         AuthFilter authFilter) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);

        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("authc", authFilter);
        factoryBean.setFilters(filters);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/crm/v1/rsa/key", "anon");
        filterChainDefinitionMap.put("/rsa/key", "anon");
        filterChainDefinitionMap.put("/crm/v1/login", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/crm/v1/is-login", "anon");
        filterChainDefinitionMap.put("/is-login", "anon");
        filterChainDefinitionMap.put("/crm/v1/logout", "anon");
        filterChainDefinitionMap.put("/logout", "anon");
        filterChainDefinitionMap.put("/swagger-ui/**", "anon");
        filterChainDefinitionMap.put("/v3/api-docs/**", "anon");
        filterChainDefinitionMap.put("/**", "authc");
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return factoryBean;
    }

    /**
     * 开启 Shiro 注解支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
