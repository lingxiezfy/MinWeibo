package com.fy.real.min.weibo.service.impl;

import com.fy.real.min.weibo.dao.dao.RelationDao;
import com.fy.real.min.weibo.dao.dao.UserDao;
import com.fy.real.min.weibo.model.entity.Relation;
import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.enums.RelationStateEnum;
import com.fy.real.min.weibo.model.exception.WeiBoException;
import com.fy.real.min.weibo.model.user.*;
import com.fy.real.min.weibo.service.IUserService;
import com.fy.real.min.weibo.service.auth.IAuthAble;
import com.fy.real.min.weibo.util.utils.ValidatorUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/22 20:17 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    UserDao userDao;
    @Autowired
    RelationDao relationDao;

    @Autowired
    IAuthAble authAble;

    @Override
    public User findUserById(Integer userId) {
        return userDao.selectByPrimaryKey(userId);
    }

    @Override
    public User findUserByUsername(String username) {
        return userDao.selectByUsername(username);
    }

    @Override
    public UserLoginView login(UserLoginRequest request) {
        ValidatorUtil.validate(request);
        User user = findUserByUsername(request.getUsername());
        if(user == null){
            throw new WeiBoException("登录失败，用户名或密码有误");
        }
        if(user.getUseful() == 0){
            throw new WeiBoException("登录失败，用户被禁止登录");
        }
        if(request.getPassword().equals(user.getPassword())){
            return UserLoginView.convertFromUser(user,authAble.auth(user));
        }
        throw new WeiBoException("登录失败，用户名或密码有误");
    }

    @Override
    public UserLoginView register(UserRegisterRequest request) {
        ValidatorUtil.validate(request);
        if(!request.getPassword().equals(request.getRePassword())){
            throw new WeiBoException("请确保两次输入的密码一致");
        }
        User user = findUserByUsername(request.getUsername());
        if(user != null){
            throw new WeiBoException("用户名已注册，请更换用户名");
        }
        User newUser = new User();
        newUser.setBir(request.getBir());
        newUser.setNickname(request.getNickname());
        newUser.setPassword(request.getPassword());
        newUser.setSex(request.getSex());
        newUser.setUsername(request.getUsername());
        userDao.insertSelective(newUser);
        newUser.setFace("");
        newUser.setFansCount(0);
        newUser.setFollowCount(0);
        newUser.setWeiboCount(0);
        return UserLoginView.convertFromUser(newUser,authAble.auth(newUser));
    }

    @Override
    public String update(User updateUser) {
        updateUser.setUpdateTime(new Date());
        return userDao.updateByPrimaryKeySelective(updateUser) > 0?updateUser.getFace():null;
    }

    @Override
    public UserView info(UserInfoRequest request) {
        User user = userDao.selectByPrimaryKey(request.getTargetUserId());
        if(user == null){
            throw  new WeiBoException("用户不存在");
        }
        UserView userView = UserView.convertFromUser(user);
        List<Relation> relations = relationDao.queryUserRelation(request.getCurrentUser().getUserId(),request.getTargetUserId());
        if(relations.size() == 1){
            Relation relation = relations.get(0);
            if (request.getCurrentUser().getUserId().equals(relation.getUserId())) {
                userView.setCurrentToThisRelation(relation.getState());
            }else {
                userView.setThisToCurrentRelation(relation.getState());
            }
        }else if(relations.size() == 2){
            Relation relation = relations.get(0);
            if (request.getCurrentUser().getUserId().equals(relation.getUserId())) {
                userView.setCurrentToThisRelation(relation.getState());
                userView.setThisToCurrentRelation(relations.get(1).getState());
            }else {
                userView.setCurrentToThisRelation(relations.get(1).getState());
                userView.setThisToCurrentRelation(relation.getState());
            }
        }
        return userView;
    }

    @Override
    public UserListResponse search(UserSearchRequest request) {
        ValidatorUtil.validate(request);
        UserListResponse response = new UserListResponse();
        Page page = PageHelper.startPage(request.getPageIndex(),request.getPageSize());
        List<User> userList = userDao.queryByName(request.getQuery());

        if(userList.size() >0){
            response.setPageIndex(page.getPageNum());
            response.setTotalCount(page.getTotal());
            response.setTotalPage(page.getPages());
            response.setList(userList.stream().map(UserView::convertFromUser).collect(Collectors.toList()));
        }else {
            response.setList(new ArrayList<>());
        }
        return response;
    }

    @Override
    public Boolean relation(RelationUserRequest request) {
        ValidatorUtil.validate(request);
        if(request.getUserId().equals(request.getCurrentUser().getUserId())){
            throw new WeiBoException("你就是你，不一样的烟火");
        }
        Relation relation = relationDao.queryUserRelationSingle(request.getCurrentUser().getUserId(),request.getUserId());
        Relation relationNew = new Relation();
        if(relation == null){
            relationNew.setUserId(request.getCurrentUser().getUserId());
            relationNew.setFollowUserId(request.getUserId());
            relationNew.setState(request.getRelation());
            relationDao.insertSelective(relationNew);
        }else {
            if(relation.getState().equals(request.getRelation())){
                throw new WeiBoException("你们两早就是这层关系了，难道还没捅破吗？");
            }
            relationNew.setRelationId(relation.getRelationId());
            relationNew.setState(request.getRelation());
            relationNew.setUpdateTime(new Date());
            relationDao.updateByPrimaryKeySelective(relationNew);
            if (RelationStateEnum.Black.getCode().equals(request.getRelation())
                    || RelationStateEnum.Default.getCode().equals(request.getRelation())) {
                if (RelationStateEnum.Concern.getCode().equals(relation.getState())) {
                    //原来关注他，现在拉黑他，自己关注数量-1，对方粉丝数-1
                    //原来关注他，现在不关注他，自己关注数量-1，对方粉丝数-1
                    User user = new User();
                    // 自己关注数-1
                    user.setUserId(request.getCurrentUser().getUserId());
                    user.setFollowCount(-1);
                    userDao.updateCountColumn(user);
                    //被关注用户粉丝数-1
                    user = new User();
                    user.setUserId(request.getUserId());
                    user.setFansCount(-1);
                    userDao.updateCountColumn(user);
                }
            }

        }
        if(RelationStateEnum.Concern.getCode().equals(request.getRelation())){
            User user = new User();
            // 自己关注数+1
            user.setUserId(request.getCurrentUser().getUserId());
            user.setFollowCount(1);
            userDao.updateCountColumn(user);
            //被关注用户粉丝数+1
            user = new User();
            user.setUserId(request.getUserId());
            user.setFansCount(1);
            userDao.updateCountColumn(user);
        }
        return true;
    }
}
