package com.fy.real.min.weibo.web.controller;

import com.fy.real.min.weibo.model.base.BaseResponse;
import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.message.SendGroupMessageRequest;
import com.fy.real.min.weibo.service.IMessageService;
import com.fy.real.min.weibo.util.utils.ValidatorUtil;
import com.fy.real.min.weibo.web.annotation.CurrentUser;
import com.fy.real.min.weibo.web.annotation.UserLoginToken;
import com.fy.real.min.weibo.web.controller.common.BaseApiController;
import com.fy.real.min.weibo.web.socket.SocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@RestController
@RequestMapping("ws")
public class WebSocketController extends BaseApiController {

    @Autowired
    IMessageService messageService;

    /**
     * 群发消息内容
     * @param message
     * @return
     */
    @GetMapping(value="sendAll")
    @UserLoginToken
    public BaseResponse sendAllMessage(@RequestParam(required=true) String message){
        BaseResponse response = new BaseResponse();
        try {
            SocketServer.BroadCastInfo(message);
        } catch (IOException e) {
            response.setCode("500");
            response.setSuccess(false);
            response.setMessage("发送失败");
        }
        return response;
    }

    /**
     * 指定会话ID发消息
     * @param message 消息内容
     * @param userId 用户Id
     * @return
     */
    @GetMapping(value="/sendOne")
    public BaseResponse sendOneMessage(@RequestParam(required=true) String message,@RequestParam(required=true) String userId){
        BaseResponse response = new BaseResponse();
        try {
            SocketServer.SendMessage(message,userId);
        } catch (IOException e) {
            response.setCode("500");
            response.setSuccess(false);
            response.setMessage("发送失败");
        }
        return response;
    }




    @PostMapping(value="/sendGroup")
    public BaseResponse<Boolean> sendGroup(@RequestBody SendGroupMessageRequest request , @CurrentUser User user){
        request.setCurrentUser(user);
        return this.exec(request,r->{
            ValidatorUtil.validate(request);

            return Boolean.TRUE;
        });
    }
}