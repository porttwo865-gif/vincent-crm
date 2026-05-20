package cn.vincent.crm.config;

import cn.vincent.common.exception.GenericException;
import cn.vincent.common.response.ResultHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 测试环境全局异常处理器 - 统一处理异常并返回标准响应格式
 */
@RestControllerAdvice
@Slf4j
public class TestGlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(GenericException.class)
    public ResultHolder<?> handleGenericException(GenericException e) {
        log.error("业务异常: {}", e.getMessage());
        return ResultHolder.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理 Shiro 认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResultHolder<?> handleAuthenticationException(AuthenticationException e) {
        log.error("认证异常: {}", e.getMessage());
        return ResultHolder.error(401, "用户名或密码错误");
    }

    /**
     * 处理 Shiro 授权异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResultHolder<?> handleUnauthorizedException(UnauthorizedException e) {
        log.error("授权异常: {}", e.getMessage());
        return ResultHolder.error(403, "无权限执行此操作");
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultHolder<?> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("参数校验失败");
        return ResultHolder.error(422, message);
    }

    /**
     * 处理其他未知异常
     */
    @ExceptionHandler(Exception.class)
    public ResultHolder<?> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResultHolder.error(500, "系统内部错误");
    }
}
