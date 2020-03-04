package com.fy.real.min.weibo.model.user;

import com.alibaba.fastjson.JSON;
import com.fy.real.min.weibo.model.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserLoginView extends UserView implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token;

    public static UserLoginView convertFromUser(User user,String token){
        if(user == null) return new UserLoginView();
        UserLoginView loginView = JSON.parseObject(JSON.toJSONString(user),UserLoginView.class);
        loginView.setToken(token);
        loginView.setAdmin(user.getAdminAble() == 1);
        return loginView;
    }
}
