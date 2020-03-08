package com.fy.real.min.weibo.model.enums;

/**
 * [Create]
 * Description: 消息类型
 * <br/>Date: 2020/3/8 13:43 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
public enum MessageTypeEnum {
    SystemNotice(1,"系统公告")
    , LikesWeiBo(2,"赞了你的微博")
    , RePostWeiBo(3,"转发了的微博")
    , RelationLike(4,"关注了你")
    , CommentWeiBo(5,"评论了你的微博")
    , ChatMessage(6,"讨论组消息")
    ;

    private int code;
    private String desc;

    MessageTypeEnum(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
