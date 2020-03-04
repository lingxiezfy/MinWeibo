package com.fy.real.min.weibo.model.weibo.comment;

import com.alibaba.fastjson.JSON;
import com.fy.real.min.weibo.model.entity.Comment;
import com.fy.real.min.weibo.model.entity.User;
import lombok.Data;

import java.io.Serializable;

/**
 * [Create]
 * Description:
 */
@Data
public class CommentViewItem implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 评论id
     */
    private Integer commentId;

    /**
     * 评论用户id
     */
    private Integer userId;
    private User author;

    /**
     * 微博id
     */
    private Integer weiboId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论点赞数
     */
    private Integer likesCount;

    private Boolean delete;

    /**
     * 创建时间
     */
    private String createTime;

    public static CommentViewItem convertFromComment(Comment comment,User author){
        if(comment == null) return null;
        CommentViewItem viewItem = JSON.parseObject(JSON.toJSONString(comment),CommentViewItem.class);
        viewItem.setDelete(comment.getUseful() != 1);
        viewItem.setAuthor(author);
        return viewItem;
    }

}
