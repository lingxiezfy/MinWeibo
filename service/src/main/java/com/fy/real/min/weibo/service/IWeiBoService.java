package com.fy.real.min.weibo.service;

import com.fy.real.min.weibo.model.weibo.PostWeiboRequest;
import com.fy.real.min.weibo.model.weibo.WeiBoListRequest;
import com.fy.real.min.weibo.model.weibo.WeiBoListResponse;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/23 20:23 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
public interface IWeiBoService {

    /**
     * [Create]
     * Description: 发送微博
     * <br/> Date: 2020/2/23 20:24
     * <br/>
     * @param request 发送微博请求
     */
    Boolean post(PostWeiboRequest request);

    /**
     * [Create]
     * Description: 获取微博列表
     * <br/> Date: 2020/2/24 13:53
     * <br/>
     * @param request 列表请求
     */
    WeiBoListResponse list(WeiBoListRequest request);
}
