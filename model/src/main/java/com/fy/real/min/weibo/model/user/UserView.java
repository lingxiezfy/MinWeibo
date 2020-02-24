package com.fy.real.min.weibo.model.user;

import com.alibaba.fastjson.JSON;
import com.fy.real.min.weibo.model.entity.User;
import lombok.Data;

import java.io.Serializable;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/22 20:23 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Data
public class UserView implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String face;

    /**
     * 性别(0男，1女)
     */
    private Integer sex;

    /**
     * 出生日期
     */
    private String bir;

    /**
     * 微博数
     */
    private Integer weiboCount;

    /**
     * 关注数
     */
    private Integer followCount;

    /**
     * 粉丝数
     */
    private Integer fansCount;

    public static UserView convertFromUser(User user){
        if(user == null) return new UserView();
        return JSON.parseObject(JSON.toJSONString(user),UserView.class);
    }
}
