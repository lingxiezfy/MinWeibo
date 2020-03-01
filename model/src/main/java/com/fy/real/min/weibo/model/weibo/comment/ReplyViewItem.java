package com.fy.real.min.weibo.model.weibo.comment;

import com.alibaba.fastjson.JSON;
import com.fy.real.min.weibo.model.entity.Reply;
import com.fy.real.min.weibo.model.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/1 21:06 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Data
public class ReplyViewItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 回复id
     */
    private Integer replyId;

    /**
     * 评论id
     */
    private Integer commentId;

    /**
     * 编写回复的用户id
     */
    private Integer fromId;
    private User fromUser;

    /**
     * 接收回复的用户id
     */
    private Integer toId;
    private User toUser;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 回复的上级id
     */
    private Integer originReplyId;

    /**
     * 回复点赞数
     */
    private Integer likesCount;

    private Boolean delete;

    /**
     * 创建时间
     */
    private String createTime;

    public static ReplyViewItem convertFromReplyWithUser(Reply reply, List<User>users){
        if(reply == null) return null;
        ReplyViewItem viewItem = JSON.parseObject(JSON.toJSONString(reply),ReplyViewItem.class);
        viewItem.setDelete(reply.getUseful() != 1);
        if(users != null && users.size() > 0){
            viewItem.setFromUser(users.stream().filter(u->u.getUserId().equals(viewItem.getFromId())).findFirst().orElse(null));
            viewItem.setToUser(users.stream().filter(u->u.getUserId().equals(viewItem.getToId())).findFirst().orElse(null));
        }
        return viewItem;
    }
}
