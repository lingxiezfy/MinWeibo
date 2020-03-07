package com.fy.real.min.weibo.service;

import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.user.*;

/**
 * [Create]
 * Description:
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
    String update(UserUpdateRequestDto updateUser);

    /**
     * [Create]
     * Description: 用户信息
     * <br/> Date: 2020/2/29 1:00
     * <br/>
     */
    UserView info(UserInfoRequest request);

    /**
     * 转换 User -> UserView 并携带二者的关系
     * @param user 转换的用户
     * @param currentUser 当前登录的用户
     * @return
     */
    UserView convertUserWithRelation(User user, User currentUser);

    /**
     * 查找用户
     */
    UserListResponse search(UserSearchRequest request);

    Boolean relation(RelationUserRequest request);

    /**
     * 用户关系列表（关注，拉黑，粉丝）
     * @param request
     * @return
     */
    UserListResponse relationList(UserRelationListRequest request);

    Boolean edit(UserEditRequest request);

    UserListResponse list(UserListRequest request);
}
