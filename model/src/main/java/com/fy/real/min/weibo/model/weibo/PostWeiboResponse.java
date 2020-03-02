package com.fy.real.min.weibo.model.weibo;

import lombok.Data;

import java.io.Serializable;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/23 20:10 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Data
public class PostWeiboResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    public PostWeiboResponse() {
    }

    public PostWeiboResponse(Integer weiBoId, Integer discussionId) {
        this.weiBoId = weiBoId;
        this.discussionId = discussionId;
    }

    private Integer weiBoId;
    private Integer discussionId;

}
