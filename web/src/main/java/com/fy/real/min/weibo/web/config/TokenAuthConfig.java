package com.fy.real.min.weibo.web.config;

import com.fy.real.min.weibo.web.interceptor.AuthenticationInterceptor;
import com.fy.real.min.weibo.web.resolver.CurrentUserMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/22 19:47 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Configuration
public class TokenAuthConfig implements WebMvcConfigurer {

    @Autowired
    AuthenticationInterceptor authenticationInterceptor;
    @Autowired
    CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserMethodArgumentResolver);
    }

}
