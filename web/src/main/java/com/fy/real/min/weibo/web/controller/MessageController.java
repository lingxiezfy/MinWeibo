package com.fy.real.min.weibo.web.controller;

import com.fy.real.min.weibo.model.base.BaseResponse;
import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.message.MessageListResponse;
import com.fy.real.min.weibo.model.message.QueryMessageRequest;
import com.fy.real.min.weibo.model.message.SystemNoticeRequest;
import com.fy.real.min.weibo.service.IMessageService;
import com.fy.real.min.weibo.web.annotation.CurrentUser;
import com.fy.real.min.weibo.web.annotation.UserLoginToken;
import com.fy.real.min.weibo.web.controller.common.BaseApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/8 17:52 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@RestController
@RequestMapping("message")
public class MessageController extends BaseApiController {

    @Autowired
    IMessageService messageService;

    @PostMapping("query")
    @UserLoginToken
    public BaseResponse<MessageListResponse> query(@RequestBody QueryMessageRequest request, @CurrentUser User user){
        request.setCurrentUser(user);
        return this.exec(request,(r)->messageService.query(r));
    }

    @GetMapping("delete/{messageId}")
    @UserLoginToken
    public BaseResponse<Boolean> delete(@PathVariable("messageId") Integer messageId, @CurrentUser User user){
        QueryMessageRequest request = new QueryMessageRequest();
        request.setMessageId(messageId);
        request.setCurrentUser(user);
        return this.exec(request,(r)->messageService.delete(r));
    }

    @PostMapping("systemNotice")
    @UserLoginToken
    public BaseResponse<Boolean> systemNotice(@RequestBody SystemNoticeRequest request, @CurrentUser User user){
        request.setCurrentUser(user);
        return this.exec(request,(r)->messageService.systemNotice(r));
    }
}
