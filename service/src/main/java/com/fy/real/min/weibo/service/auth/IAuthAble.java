package com.fy.real.min.weibo.service.auth;

import com.fy.real.min.weibo.model.entity.User;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
public interface IAuthAble {
    /**
     * [Create]
     * Description: 为用户创建验证
     * <br/> Date: 2020/2/23 1:02
     * <br/>
     * @param user 用户
     */
    String auth(User user);

    /**
     * [Create]
     * Description: 验证token，得到用户信息
     * <br/> Date: 2020/2/23 1:04
     * <br/>
     * @param token token
     */
    User verify(String token);
}
