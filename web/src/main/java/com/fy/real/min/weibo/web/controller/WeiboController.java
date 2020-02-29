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
 * <br/>Date: 2020/2/20 13:06 - Create
 *
 * @author fengyu.zhang
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
    public BaseResponse post(HttpServletRequest request, @CurrentUser User user){
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        String content = params.getParameter("content");
        if(StringUtils.isBlank(content)){
            BaseResponse response = new BaseResponse(ResponseCodeEnum.Response_600);
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
    public BaseResponse<WeiBoListResponse> listAll(@RequestBody WeiBoListRequest request){
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
        DeleteWeiBoRequest request = new DeleteWeiBoRequest();
        request.setWeiBoId(weiBoId);
        request.setCurrentUser(user);
        return this.exec(request,(r)->weiBoService.delete(r));
    }

}
