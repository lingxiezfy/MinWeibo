package com.fy.real.min.weibo.service;

import com.fy.real.min.weibo.model.discussion.DiscussionInfoRequest;
import com.fy.real.min.weibo.model.discussion.DiscussionViewItem;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
public interface IDiscussionService {

    DiscussionViewItem info(DiscussionInfoRequest request);
}
