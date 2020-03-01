package com.fy.real.min.weibo.model.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * reply
 * @author 
 */
public class Reply implements Serializable {
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

    /**
     * 接收回复的用户id
     */
    private Integer toId;

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

    /**
     * 是否有效（0：无效，1有效）
     */
    private Integer useful;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public Integer getOriginReplyId() {
        return originReplyId;
    }

    public void setOriginReplyId(Integer originReplyId) {
        this.originReplyId = originReplyId;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getUseful() {
        return useful;
    }

    public void setUseful(Integer useful) {
        this.useful = useful;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}