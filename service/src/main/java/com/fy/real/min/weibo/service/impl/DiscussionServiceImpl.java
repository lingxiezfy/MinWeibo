package com.fy.real.min.weibo.service.impl;

import com.fy.real.min.weibo.dao.dao.DiscussionDao;
import com.fy.real.min.weibo.dao.dao.UserDao;
import com.fy.real.min.weibo.dao.dao.WeiboDao;
import com.fy.real.min.weibo.model.discussion.DiscussionInfoRequest;
import com.fy.real.min.weibo.model.discussion.DiscussionViewItem;
import com.fy.real.min.weibo.model.entity.Discussion;
import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.entity.Weibo;
import com.fy.real.min.weibo.model.enums.ResponseCodeEnum;
import com.fy.real.min.weibo.model.exception.WeiBoException;
import com.fy.real.min.weibo.model.user.UserView;
import com.fy.real.min.weibo.service.IDiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@Service
public class DiscussionServiceImpl implements IDiscussionService {

    @Autowired
    DiscussionDao discussionDao;
    @Autowired
    WeiboDao weiboDao;
    @Autowired
    UserDao userDao;

    @Override
    public DiscussionViewItem info(DiscussionInfoRequest request) {
        Discussion discussion = discussionDao.selectByPrimaryKey(request.getDiscussionId());
        if(discussion == null){
            throw new WeiBoException(ResponseCodeEnum.Response_601,"讨论组不存在");
        }
        Date now = new Date();
        long mills = discussion.getAliveTime().getTime() - now.getTime();
        if(mills <= 0){
            throw new WeiBoException(ResponseCodeEnum.Response_601, "讨论组已过期");
        }
        User author = userDao.selectByPrimaryKey(discussion.getUserId());
        if(author == null){
            throw new WeiBoException(ResponseCodeEnum.Response_601,"用户不存在");
        }
        Weibo weibo = weiboDao.selectByPrimaryKey(discussion.getWeiboId());
        DiscussionViewItem viewItem = DiscussionViewItem.convertFromDiscussion(discussion);
        viewItem.setContent(weibo.getContent());
        viewItem.setAuthor(UserView.convertFromUser(author));
        viewItem.setCurrent(UserView.convertFromUser(request.getCurrentUser()));
        viewItem.setAliveMinutes(mills/1000);
        return viewItem;
    }
}
