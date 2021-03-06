package com.fy.real.min.weibo.web.controller;

import com.fy.real.min.weibo.model.base.BaseResponse;
import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.enums.ResponseCodeEnum;
import com.fy.real.min.weibo.model.weibo.*;
import com.fy.real.min.weibo.service.IWeiBoService;
import com.fy.real.min.weibo.util.utils.UploadImageUtils;
import com.fy.real.min.weibo.web.annotation.CurrentUser;
import com.fy.real.min.weibo.web.annotation.UserLoginToken;
import com.fy.real.min.weibo.web.controller.common.BaseApiController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@RestController
@RequestMapping("weibo")
public class WeiboController extends BaseApiController {

    @Autowired
    IWeiBoService weiBoService;

    /**
     * [Create]
     * Description: 发微博
     * <br/> Date: 2020/2/23 20:24
     * <br/>
     */
    @UserLoginToken
    @PostMapping("post")
    public BaseResponse<PostWeiboResponse> post(HttpServletRequest request, @CurrentUser User user){
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        String content = params.getParameter("content");
        if(StringUtils.isBlank(content)){
            BaseResponse<PostWeiboResponse> response = new BaseResponse<>(ResponseCodeEnum.Response_600);
            response.setSuccess(false);
            response.setMessage("微博消息不能为空");
            return response;
        }
        PostWeiboRequest postWeiboRequest = new PostWeiboRequest();
        postWeiboRequest.setContent(content);
        List<MultipartFile> pics = params.getFiles("pic");
        if (pics != null && pics.size() > 0) {
            String tempPath = request.getSession().getServletContext().getRealPath("") + "upload";
            String realPath = "web/src/main/resources/static/upload";
            postWeiboRequest.setPicList(UploadImageUtils.upload(pics,tempPath,realPath));
        }
        postWeiboRequest.setUser(user);
        return this.exec(postWeiboRequest,(r)->weiBoService.post(r));
    }

    /**
     * [Create]
     * Description: 编辑微博
     * <br/> Date: 2020/2/23 20:24
     * <br/>
     */
    @UserLoginToken
    @PostMapping("edit")
    public BaseResponse<Boolean> edit(HttpServletRequest request, @CurrentUser User user){
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        String content = params.getParameter("content");
        int weiBoId = 0;
        try{
            weiBoId = Integer.parseInt(params.getParameter("weiBoId"));
        }catch (Exception e){
            weiBoId = 0;
        }
        if(StringUtils.isBlank(content) || weiBoId <= 0){
            BaseResponse<Boolean> response = new BaseResponse<>(ResponseCodeEnum.Response_600);
            response.setSuccess(false);
            response.setMessage("微博不能为空");
            return response;
        }
        PostWeiboRequest postWeiboRequest = new PostWeiboRequest();
        postWeiboRequest.setContent(content);
        List<MultipartFile> pics = params.getFiles("pic");
        if (pics != null && pics.size() > 0) {
            String tempPath = request.getSession().getServletContext().getRealPath("") + "upload";
            String realPath = "web/src/main/resources/static/upload";
            postWeiboRequest.setPicList(UploadImageUtils.upload(pics,tempPath,realPath));
        }
        postWeiboRequest.setWeiBoId(weiBoId);
        postWeiboRequest.setUser(user);
        return this.exec(postWeiboRequest,(r)->weiBoService.edit(r));
    }

    @UserLoginToken
    @PostMapping("rePost")
    public BaseResponse rePost(@RequestBody RePostWeiboRequest request, @CurrentUser User user){
        request.setCurrentUser(user);
        return this.exec(request,(r)->weiBoService.rePost(r));
    }


    /**
     * [Create]
     * Description: 获取用户微博列表
     * <br/> Date: 2020/2/24 13:52
     * <br/>
     * @param request 列表请求
     * @param user 当前登录用户
     */
    @UserLoginToken
    @PostMapping("list")
    public BaseResponse<WeiBoListResponse> list(@RequestBody WeiBoListRequest request, @CurrentUser User user){
        request.setCurrentUser(user);
        return this.exec(request,(r)->weiBoService.list(r));
    }

    /**
     * 浏览所有微博
     * @param request 列表请求
     */
    @PostMapping("listAll")
    @UserLoginToken
    public BaseResponse<WeiBoListResponse> listAll(@RequestBody WeiBoListRequest request, @CurrentUser User user){
        request.setCurrentUser(user);
        return this.exec(request,(r)->weiBoService.list(r));
    }

    @PostMapping("search")
    @UserLoginToken
    public BaseResponse<WeiBoListResponse> search(@RequestBody WeiBoSearchRequest request, @CurrentUser User user){
        request.setCurrentUser(user);
        return this.exec(request,(r)->weiBoService.search(r));
    }

    @GetMapping("delete/{weiBoId}")
    @UserLoginToken
    public BaseResponse<Boolean> delete(@PathVariable("weiBoId") Integer weiBoId, @CurrentUser User user){
        WeiBoActionRequest request = new WeiBoActionRequest();
        request.setWeiBoId(weiBoId);
        request.setCurrentUser(user);
        return this.exec(request,(r)->weiBoService.delete(r));
    }

    @GetMapping("collect/{weiBoId}")
    @UserLoginToken
    public BaseResponse<Integer> collect(@PathVariable("weiBoId") Integer weiBoId, @CurrentUser User user){
        WeiBoActionRequest request = new WeiBoActionRequest();
        request.setWeiBoId(weiBoId);
        request.setCurrentUser(user);
        return this.exec(request,(r)->weiBoService.collect(r));
    }


    @PostMapping("collect/list")
    @UserLoginToken
    public BaseResponse<WeiBoListResponse> userCollect(@RequestBody WeiBoSearchRequest request, @CurrentUser User user){
        request.setCurrentUser(user);
        return this.exec(request,(r)->weiBoService.userCollect(r));
    }

    @GetMapping("collect/cancel/{weiBoId}")
    @UserLoginToken
    public BaseResponse<Integer> cancelCollect(@PathVariable("weiBoId") Integer weiBoId, @CurrentUser User user){
        WeiBoActionRequest request = new WeiBoActionRequest();
        request.setWeiBoId(weiBoId);
        request.setCurrentUser(user);
        return this.exec(request,(r)->weiBoService.cancelCollect(r));
    }

    @GetMapping("likes/{weiBoId}")
    @UserLoginToken
    public BaseResponse<Integer> likes(@PathVariable("weiBoId") Integer weiBoId, @CurrentUser User user){
        WeiBoActionRequest request = new WeiBoActionRequest();
        request.setWeiBoId(weiBoId);
        request.setCurrentUser(user);
        return this.exec(request,(r)->weiBoService.likes(r));
    }

    @GetMapping("likes/cancel/{weiBoId}")
    @UserLoginToken
    public BaseResponse<Integer> cancelLikes(@PathVariable("weiBoId") Integer weiBoId, @CurrentUser User user){
        WeiBoActionRequest request = new WeiBoActionRequest();
        request.setWeiBoId(weiBoId);
        request.setCurrentUser(user);
        return this.exec(request,(r)->weiBoService.cancelLikes(r));
    }

}
