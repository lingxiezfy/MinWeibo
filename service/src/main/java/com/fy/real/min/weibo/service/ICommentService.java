package com.fy.real.min.weibo.service;

import com.fy.real.min.weibo.model.weibo.comment.*;

/**
 * [Create]
 * Description:
 */
public interface ICommentService {
    CommentListResponse list(CommentListRequest request);

    CommentViewItem add(CommentRequest request);

    Boolean delete(CommentActionRequest request);

    Integer likes(CommentActionRequest request);

    Integer cancelLikes(CommentActionRequest request);
}
