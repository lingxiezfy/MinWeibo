package com.fy.real.min.weibo.model.discussion;

import com.alibaba.fastjson.JSON;
import com.fy.real.min.weibo.model.entity.Discussion;
import com.fy.real.min.weibo.model.user.UserView;
import lombok.Data;

/**
 * [Create]
 * Description:
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
    private UserView author;
    private UserView current;

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
     * 存活秒数
     */
    private long aliveMinutes;

    /**
     * 创建时间
     */
    private String createTime;

    public static DiscussionViewItem convertFromDiscussion(Discussion discussion) {
        return JSON.parseObject(JSON.toJSONString(discussion),DiscussionViewItem.class);
    }
}
