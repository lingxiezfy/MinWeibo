package com.fy.real.min.weibo.model.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.user.UserView;
import lombok.Data;

import java.util.Date;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@Data
public class MessageBase {
    private String messageType;
    private UserView author;
    private String content;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;
    // 附加内容
    private Object third;

    public MessageBase() {
    }

    public MessageBase(User author, String messageType, String content, Date sendTime) {
        this.author = UserView.convertFromUser(author);
        this.content = content;
        this.sendTime = sendTime;
        this.messageType = messageType;
    }

    public MessageBase(User author, String messageType, String content, Date sendTime,Object third) {
        this(author,messageType,content,sendTime);
        this.third = third;
    }
}
