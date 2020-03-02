package com.fy.real.min.weibo.model.discussion;

import com.fy.real.min.weibo.model.base.BaseRequest;
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
public class DiscussionInfoRequest extends BaseRequest {
    /**
     * 讨论组Id
     */
    private Integer discussionId;
}
