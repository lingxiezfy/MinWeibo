package com.fy.real.min.weibo.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fy.real.min.weibo.model.base.BaseResponse;
import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.enums.ResponseCodeEnum;
import com.fy.real.min.weibo.service.auth.IAuthAble;
import com.fy.real.min.weibo.web.annotation.UserLoginToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);


    @Value("${login.current.user.key}")
    private String currentLoginUserKey;
    @Value("${login.token.header.key}")
    private String tokenHeaderKey;

    @Autowired
    @Qualifier("JWTAuth")
    IAuthAble authAble;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        logger.info(String.format("请求[%s];",request.getRequestURI()));
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if(!userLoginToken.required()){
                return true;
            }
            String token = request.getHeader(tokenHeaderKey);
            logger.info(String.format("Token校验[%s];",token));
            //执行认证
            if (token == null){
                sendLoginRefuseMessage(response);
                logger.info("Token校验[未通过];");
                return false;
            }
            User user = authAble.verify(token);
            if(user == null){
                sendLoginRefuseMessage(response,"登录已过期，请重新登录");
                logger.info("Token校验[未通过];verify refused!");
                return false;
            }
            request.setAttribute(currentLoginUserKey, user);
            logger.info("Token校验[通过];");
        }
        return true;
    }

    private void sendLoginRefuseMessage(HttpServletResponse response) throws IOException {
        sendLoginRefuseMessage(response,null);
    }
    private void sendLoginRefuseMessage(HttpServletResponse response, String message) throws IOException {
        BaseResponse res = new BaseResponse(ResponseCodeEnum.Response_301);
        if(StringUtils.isNoneBlank(message)){
            res.setMessage(message);
        }
        res.setSuccess(false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSONObject.toJSONString(res));
        writer.flush();
    }
}
