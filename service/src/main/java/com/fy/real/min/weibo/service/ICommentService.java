package com.fy.real.min.weibo.service;

import com.fy.real.min.weibo.model.weibo.comment.*;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/1 21:37 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
public interface ICommentService {
    CommentListResponse list(CommentListRequest request);

    CommentViewItem add(CommentRequest request);

    Integer likes(CommentActionRequest request);

    Integer cancelLikes(CommentActionRequest request);
}
