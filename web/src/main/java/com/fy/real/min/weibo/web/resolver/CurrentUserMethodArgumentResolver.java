package com.fy.real.min.weibo.web.resolver;

import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.web.annotation.CurrentUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@Component
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver{

    @Value("${login.current.user.key}")
    private String currentLoginUserKey;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(User.class) && methodParameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        return (User)nativeWebRequest.getAttribute(currentLoginUserKey, RequestAttributes.SCOPE_REQUEST);
    }
}
