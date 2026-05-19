package cn.vincent.crm.app.handler;

import cn.vincent.common.response.ResultHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一响应包装 - 自动将 Controller 返回值包装为 ResultHolder 格式
 * <p>
 * 前端期望统一的响应格式：{code, message, data}
 * 通过 ResponseBodyAdvice 自动包装，Controller 无需手动调用 ResultHolder.success()
 */
@RestControllerAdvice(basePackages = "cn.vincent.crm")
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否需要包装
     * <p>
     * 以下情况不包装：
     * 1. 返回类型已经是 ResultHolder
     * 2. 返回类型为 void（Spring 会返回 null）
     * 3. 错误处理已由 GlobalExceptionHandler 处理
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 不包装 ResultHolder 类型（已经是标准格式）
        return !ResultHolder.class.isAssignableFrom(returnType.getParameterType());
    }

    /**
     * 包装响应体
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // body 为 null 时包装为成功响应（data 为 null）
        if (body == null) {
            return ResultHolder.success(null);
        }
        // 已是 ResultHolder 则直接返回
        if (body instanceof ResultHolder) {
            return body;
        }
        // 包装为成功响应
        return ResultHolder.success(body);
    }
}
