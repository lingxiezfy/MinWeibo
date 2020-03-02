package com.fy.real.min.weibo.model.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.fy.real.min.weibo.model.user.UserView;
import lombok.Data;

import java.util.Date;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/2 20:11 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Data
public class MessageBase {
    private UserView author;
    private String content;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    public MessageBase() {
    }

    public MessageBase(UserView author, String content, Date sendTime) {
        this.author = author;
        this.content = content;
        this.sendTime = sendTime;
    }
}
