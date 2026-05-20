package cn.vincent.crm.config;

import cn.vincent.common.response.ResultHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 测试环境统一响应包装 - 自动将 Controller 返回值包装为 ResultHolder 格式
 */
@RestControllerAdvice(basePackages = "cn.vincent.crm")
public class TestResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否需要包装 - 已是 ResultHolder 则跳过
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !ResultHolder.class.isAssignableFrom(returnType.getParameterType());
    }

    /**
     * 包装响应体
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return ResultHolder.success(null);
        }
        if (body instanceof ResultHolder) {
            return body;
        }
        return ResultHolder.success(body);
    }
}
