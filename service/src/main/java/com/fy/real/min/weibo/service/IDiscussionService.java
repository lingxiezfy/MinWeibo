package com.fy.real.min.weibo.service;

import com.fy.real.min.weibo.model.discussion.DiscussionInfoRequest;
import com.fy.real.min.weibo.model.discussion.DiscussionViewItem;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/2 17:10 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
public interface IDiscussionService {

    DiscussionViewItem info(DiscussionInfoRequest request);
}
