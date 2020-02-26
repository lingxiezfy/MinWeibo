package com.fy.real.min.weibo.service;

import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.user.*;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/22 20:12 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
public interface IUserService {
    /**
     * [Create]
     * Description: 根据Id查找用户
     * <br/> Date: 2020/2/22 23:36
     * <br/>
     * @param userId 用户id
     */
    User findUserById(Integer userId);

    /**
     * [Create]
     * Description: 根据用户名查找用户
     * <br/> Date: 2020/2/22 23:40
     * <br/>
     * @param username 用户名
     */
    User findUserByUsername(String username);

    /**
     * [Create]
     * Description: 用户登录
     * <br/> Date: 2020/2/23 1:45
     * <br/>
     * @param request 登录请求
     */
    UserLoginView login(UserLoginRequest request);

    /**
     * [Create]
     * Description: 用户注册
     * <br/> Date: 2020/2/23 1:45
     * <br/>
     * @param request 注册请求
     */
    UserLoginView register(UserRegisterRequest request);

    /**
     * [Create]
     * Description: 更新用户信息
     * <br/> Date: 2020/2/25 12:22
     * <br/>
     * @param updateUser 更新用户
     */
    String update(User updateUser);

    UserView info(Integer userId);

    UserListResponse search(UserSearchRequest request);
}
