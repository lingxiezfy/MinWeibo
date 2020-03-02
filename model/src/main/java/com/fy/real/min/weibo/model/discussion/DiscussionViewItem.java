package com.fy.real.min.weibo.model.discussion;

import com.alibaba.fastjson.JSON;
import com.fy.real.min.weibo.model.entity.Discussion;
import lombok.Data;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/2 17:06 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Data
public class DiscussionViewItem {
    /**
     * 讨论组Id
     */
    private Integer discussionId;

    /**
     * 群主id
     */
    private Integer userId;
    private String nickname;

    /**
     * 关联的微博id
     */
    private Integer weiboId;
    private String content;
    /**
     * 存活到期时间
     */
    private String aliveTime;

    /**
     * 创建时间
     */
    private String createTime;

    public static DiscussionViewItem convertFromDiscussion(Discussion discussion) {
        return JSON.parseObject(JSON.toJSONString(discussion),DiscussionViewItem.class);
    }
}
