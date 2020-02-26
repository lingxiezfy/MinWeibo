package com.fy.real.min.weibo.web.controller;

import com.fy.real.min.weibo.model.base.BaseResponse;
import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.user.*;
import com.fy.real.min.weibo.service.IUserService;
import com.fy.real.min.weibo.util.utils.UploadImageUtils;
import com.fy.real.min.weibo.web.annotation.CurrentUser;
import com.fy.real.min.weibo.web.annotation.UserLoginToken;
import com.fy.real.min.weibo.web.controller.common.BaseApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/22 20:19 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseApiController {
    @Autowired
    IUserService userService;

    /**
     * [Create]
     * Description: 用户登录
     * <br/> Date: 2020/2/23 1:45
     * <br/>
     * @param request 登录请求
     */
    @PostMapping("login")
    public BaseResponse<UserLoginView> login(@RequestBody UserLoginRequest request){
        return this.exec(request,(r)->userService.login(r));
    }

    /**
     * [Create]
     * Description: 获取用户信息
     * <br/> Date: 2020/2/23 1:45
     * <br/>
     */
    @GetMapping("info/{userId}")
    @UserLoginToken
    public BaseResponse<UserView> info(@PathVariable("userId") Integer userId, @CurrentUser User user){
        if (userId == null || userId == 0 || user.getUserId().equals(userId)) {
            BaseResponse<UserView> response = new BaseResponse<>();
            response.setResult(UserView.convertFromUser(user));
            return response;
        }
        return this.exec(userId,i->userService.info(i));
    }

    @PostMapping("update")
    @UserLoginToken
    public BaseResponse<String> update(@RequestBody UserUpdateRequest request, @CurrentUser User user, HttpServletRequest servletRequest){
        String face = null;
        if(request.getFaceBase64() != null&&request.getFaceBase64().length() >0){
            String tempPath = servletRequest.getSession().getServletContext().getRealPath("") + "upload";
            String realPath = "web/src/main/resources/static/upload";
            face = UploadImageUtils.upload(request.getFaceBase64(),tempPath,realPath);
        }
        User updateUser = new User();
        updateUser.setUserId(user.getUserId());
        updateUser.setBir(request.getBir());
        updateUser.setNickname(request.getNickname());
        updateUser.setFace(face);
        return this.exec(updateUser,(u)->userService.update(u));
    }

    /**
     * [Create]
     * Description: 用户注册
     * <br/> Date: 2020/2/23 1:45
     * <br/>
     * @param request 注册请求
     */
    @PostMapping("register")
    public BaseResponse<UserLoginView> register(@RequestBody UserRegisterRequest request){
        return this.exec(request,(r)->userService.register(r));
    }

    @GetMapping("loginOut")
    @UserLoginToken
    public BaseResponse loginOut(@CurrentUser User user){
        return new BaseResponse();
    }

}
