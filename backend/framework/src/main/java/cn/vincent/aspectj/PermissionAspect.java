package cn.vincent.aspectj;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.exception.GenericException;
import cn.vincent.common.response.CrmHttpResultCode;
import cn.vincent.context.OrganizationContext;
import cn.vincent.security.PermissionCache;
import cn.vincent.security.SessionUtils;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 权限校验切面 - 拦截 @RequiresPermissions 注解进行权限检查
 */
@Aspect
@Component
public class PermissionAspect {

    /** 权限缓存 */
    @Resource
    private PermissionCache permissionCache;

    /**
     * 环绕通知 - 校验当前用户是否拥有所需权限
     *
     * @param joinPoint            切点
     * @param requiresPermissions  权限注解
     * @return 方法执行结果
     * @throws Throwable 方法执行异常或权限不足异常
     */
    @Around("@annotation(requiresPermissions)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequiresPermissions requiresPermissions) throws Throwable {
        String userId = SessionUtils.getUserId();
        String orgId = OrganizationContext.getOrganizationId();

        // 获取用户权限列表
        List<String> userPermissions = permissionCache.getPermissionIds(userId, orgId);

        String[] required = requiresPermissions.value();
        RequiresPermissions.Logical logical = requiresPermissions.logical();

        // 根据逻辑关系校验权限
        boolean hasPermission;
        if (logical == RequiresPermissions.Logical.AND) {
            // AND 逻辑：需要同时拥有所有权限
            hasPermission = Arrays.stream(required).allMatch(userPermissions::contains);
        } else {
            // OR 逻辑：拥有任一权限即可
            hasPermission = Arrays.stream(required).anyMatch(userPermissions::contains);
        }

        if (!hasPermission) {
            throw new GenericException(CrmHttpResultCode.FORBIDDEN);
        }

        return joinPoint.proceed();
    }
}
