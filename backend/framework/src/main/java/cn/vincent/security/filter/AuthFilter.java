package cn.vincent.security.filter;

import cn.vincent.common.response.CrmHttpResultCode;
import cn.vincent.common.response.ResultHolder;
import cn.vincent.security.dto.SessionUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 认证过滤器 - 校验 Shiro Session 是否有效
 * <p>
 * 未认证请求返回 401 状态码，并在响应头中设置 AUTHENTICATION_STATUS
 */
public class AuthFilter extends AccessControlFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    /** 响应头标识认证状态 */
    private static final String AUTH_STATUS_HEADER = "AUTHENTICATION_STATUS";

    /** JSON 序列化器 */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 判断是否允许访问 - 已认证用户放行
     *
     * @param request     请求
     * @param response    响应
     * @param mappedValue 映射值
     * @return true 表示放行
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            Object principal = getSubject(request, response).getPrincipal();
            if (principal instanceof SessionUser) {
                return true;
            }
        } catch (Exception e) {
            log.warn("认证检查异常: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 拒绝访问处理 - 返回 401 状态码
     *
     * @param request  请求
     * @param response 响应
     * @return false 表示不继续处理
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        try {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader(AUTH_STATUS_HEADER, "unauthenticated");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json;charset=UTF-8");

            ResultHolder<?> result = ResultHolder.error(
                    CrmHttpResultCode.UNAUTHORIZED.getCode(),
                    CrmHttpResultCode.UNAUTHORIZED.getMessage());
            httpResponse.getWriter().write(objectMapper.writeValueAsString(result));
            httpResponse.getWriter().flush();
        } catch (IOException e) {
            log.error("返回 401 响应失败: {}", e.getMessage(), e);
        }
        return false;
    }
}
