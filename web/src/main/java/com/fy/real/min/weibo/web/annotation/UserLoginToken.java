package com.fy.real.min.weibo.web.annotation;

import java.lang.annotation.*;

/**
 * [Create]
 * Description: 验证用户权限
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserLoginToken {
    boolean required() default true;
}
