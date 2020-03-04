package com.fy.real.min.weibo.web.controller;

import com.fy.real.min.weibo.model.base.BaseResponse;
import com.fy.real.min.weibo.model.discussion.DiscussionInfoRequest;
import com.fy.real.min.weibo.model.discussion.DiscussionViewItem;
import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.service.IDiscussionService;
import com.fy.real.min.weibo.web.annotation.CurrentUser;
import com.fy.real.min.weibo.web.annotation.UserLoginToken;
import com.fy.real.min.weibo.web.controller.common.BaseApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/2 17:02 - Create
 *
 * @version 1.0
 */
@RestController
@RequestMapping("discussion")
public class DiscussionController extends BaseApiController {

    @Autowired
    IDiscussionService discussionService;

    @UserLoginToken
    @GetMapping("info/{discussionId}")
    public BaseResponse<DiscussionViewItem> info(@PathVariable("discussionId") Integer discussionId, @CurrentUser User user){
        DiscussionInfoRequest request = new DiscussionInfoRequest();
        request.setDiscussionId(discussionId);
        request.setCurrentUser(user);
        return this.exec(discussionId,(id)->discussionService.info(request));
    }
}
