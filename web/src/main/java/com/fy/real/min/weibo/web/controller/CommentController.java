package com.fy.real.min.weibo.web.controller;

import com.fy.real.min.weibo.model.base.BaseResponse;
import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.weibo.comment.*;
import com.fy.real.min.weibo.service.ICommentService;
import com.fy.real.min.weibo.web.annotation.CurrentUser;
import com.fy.real.min.weibo.web.annotation.UserLoginToken;
import com.fy.real.min.weibo.web.controller.common.BaseApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/1 20:38 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@RestController
@RequestMapping("comment")
public class CommentController extends BaseApiController {

    @Autowired
    ICommentService commentService;

    @PostMapping("add")
    @UserLoginToken
    public BaseResponse<CommentViewItem> add(@RequestBody CommentRequest request, @CurrentUser User user){
        request.setCurrentUser(user);
        return this.exec(request,(r)->commentService.add(r));
    }

    @PostMapping("list")
    @UserLoginToken
    public BaseResponse<CommentListResponse> list(@RequestBody CommentListRequest request, @CurrentUser User user){
        request.setCurrentUser(user);
        return this.exec(request,r->commentService.list(r));
    }

    @GetMapping("likes/{commentId}")
    @UserLoginToken
    public BaseResponse<Integer> likes(@PathVariable("commentId") Integer commentId,@CurrentUser User user){
        CommentActionRequest request = new CommentActionRequest();
        request.setCommentId(commentId);
        request.setCurrentUser(user);
        return this.exec(request,(r)->commentService.likes(r));
    }

    @GetMapping("cancelLikes/{commentId}")
    @UserLoginToken
    public BaseResponse<Integer> cancelLikes(@PathVariable("commentId") Integer commentId,@CurrentUser User user){
        CommentActionRequest request = new CommentActionRequest();
        request.setCommentId(commentId);
        request.setCurrentUser(user);
        return this.exec(request,(r)->commentService.cancelLikes(r));
    }
}
