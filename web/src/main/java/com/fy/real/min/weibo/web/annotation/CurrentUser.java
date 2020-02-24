package com.fy.real.min.weibo.web.annotation;

import java.lang.annotation.*;

/**
 * [Create]
 * Description: 当前登录用户
 * <br/>Date: 2020/2/22 20:39 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
}
