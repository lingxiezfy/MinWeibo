package com.fy.real.min.weibo.model.message;

import com.fy.real.min.weibo.model.entity.Message;
import com.fy.real.min.weibo.model.user.UserView;
import lombok.Data;

import java.io.Serializable;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/8 0:06 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Data
public class MessageViewItem extends Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private UserView fromUser;
    private UserView toUser;
    /**
     * 相关信息
     */
    private Object third;
}
