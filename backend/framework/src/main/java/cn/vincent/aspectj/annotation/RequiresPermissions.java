package cn.vincent.aspectj.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验注解 - 后续会集成 Shiro 实现
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermissions {

    /** 权限标识列表 */
    String[] value();

    /** 多权限之间的逻辑关系，默认 AND */
    Logical logical() default Logical.AND;

    /**
     * 权限逻辑枚举
     */
    enum Logical {
        /** 需要同时满足所有权限 */
        AND,
        /** 满足任一权限即可 */
        OR
    }
}
