package com.fy.real.min.weibo.service.impl;

import com.fy.real.min.weibo.dao.dao.DiscussionDao;
import com.fy.real.min.weibo.dao.dao.WeiboDao;
import com.fy.real.min.weibo.model.discussion.DiscussionInfoRequest;
import com.fy.real.min.weibo.model.discussion.DiscussionViewItem;
import com.fy.real.min.weibo.model.entity.Discussion;
import com.fy.real.min.weibo.model.entity.Weibo;
import com.fy.real.min.weibo.model.exception.WeiBoException;
import com.fy.real.min.weibo.service.IDiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/2 17:11 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Service
public class DiscussionServiceImpl implements IDiscussionService {

    @Autowired
    DiscussionDao discussionDao;
    @Autowired
    WeiboDao weiboDao;

    @Override
    public DiscussionViewItem info(DiscussionInfoRequest request) {
        Discussion discussion = discussionDao.selectByPrimaryKey(request.getDiscussionId());
        if(discussion == null){
            throw new WeiBoException("讨论组不存在");
        }
        if(discussion.getAliveTime().getTime()<= (new Date()).getTime()){
            throw new WeiBoException("讨论组已过期");
        }
        Weibo weibo = weiboDao.selectByPrimaryKey(discussion.getWeiboId());
        DiscussionViewItem viewItem = DiscussionViewItem.convertFromDiscussion(discussion);
        viewItem.setContent(weibo.getContent());
        viewItem.setNickname(request.getCurrentUser().getNickname());
        return viewItem;
    }
}
