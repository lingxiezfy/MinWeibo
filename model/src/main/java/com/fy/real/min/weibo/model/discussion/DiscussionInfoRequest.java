package com.fy.real.min.weibo.model.discussion;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@Data
public class DiscussionInfoRequest extends BaseRequest {
    /**
     * 讨论组Id
     */
    private Integer discussionId;
}
