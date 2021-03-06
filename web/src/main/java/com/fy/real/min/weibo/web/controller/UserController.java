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
        UserInfoRequest request = new UserInfoRequest();
        request.setCurrentUser(user);
        request.setTargetUserId(userId);
        return this.exec(request,r->userService.info(r));
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
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto();

        requestDto.setUserId(user.getUserId());
        requestDto.setBir(request.getBir());
        requestDto.setNickname(request.getNickname());
        requestDto.setFace(face);
        requestDto.setPassword(request.getPassword());
        requestDto.setOldPassword(request.getOldPassword());
        requestDto.setCurrentUser(user);
        return this.exec(requestDto,(u)->userService.update(u));
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

    @PostMapping("search")
    @UserLoginToken
    public BaseResponse<UserListResponse> search(@RequestBody UserSearchRequest request,@CurrentUser User user){
        request.setCurrentUser(user);
        return this.exec(request,(r)->userService.search(r));
    }

    @PostMapping("list")
    @UserLoginToken
    public BaseResponse<UserListResponse> list(UserListRequest request,@CurrentUser User user){
        request.setCurrentUser(user);
        return this.exec(request,(r)->userService.list(r));
    }

    @PostMapping("edit")
    @UserLoginToken
    public BaseResponse<Boolean> edit(UserEditRequest request,@CurrentUser User user){
        request.setCurrentUser(user);
        return this.exec(request,(r)->userService.edit(request));
    }

    @PostMapping("relation")
    @UserLoginToken
    public BaseResponse relation(@RequestBody RelationUserRequest request,@CurrentUser User user){
        request.setCurrentUser(user);
        return this.exec(request,(r)->userService.relation(request));
    }

    @PostMapping("relation/list")
    @UserLoginToken
    public BaseResponse<UserListResponse> relationList(@RequestBody UserRelationListRequest request,@CurrentUser User user){
        request.setCurrentUser(user);
        return this.exec(request,(r)->userService.relationList(r));
    }

}
