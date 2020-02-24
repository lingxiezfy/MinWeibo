package com.fy.real.min.weibo.service.auth.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.service.IUserService;
import com.fy.real.min.weibo.service.auth.IAuthAble;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/23 1:07 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Component("JWTAuth")
public class JWTAuth implements IAuthAble {

    @Autowired
    IUserService userService;

    @Value("${login.auth.expiry.time.second}")
    private Integer loginAuthExpiryTimeSecond;

    @Override
    public String auth(User user) {
        Date start = new Date();
        // 到期时间
        long currentTime = System.currentTimeMillis() + (loginAuthExpiryTimeSecond * 1000);
        Date end = new Date(currentTime);
        return JWT.create().withAudience(user.getUserId().toString()).withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256(user.getPassword()));
    }

    @Override
    public User verify(String token) {
        if(StringUtils.isBlank(token)){
            return null;
        }
        try {
            String userIdStr = JWT.decode(token).getAudience().get(0);
            if (!StringUtils.isNumeric(userIdStr)) {
                return null;
            }
            User user = userService.findUserById(Integer.parseInt(userIdStr));
            if (user == null) {
                return null;
            }
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            jwtVerifier.verify(token);
            return user;
        }catch (Exception e){
            return null;
        }
    }
}
