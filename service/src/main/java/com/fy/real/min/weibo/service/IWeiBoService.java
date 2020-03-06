package com.fy.real.min.weibo.service;

import com.fy.real.min.weibo.model.weibo.*;

/**
 * [Create]
 * Description:
 */
public interface IWeiBoService {

    /**
     * [Create]
     * Description: 发送微博
     * <br/> Date: 2020/2/23 20:24
     * <br/>
     * @param request 发送微博请求
     */
    PostWeiboResponse post(PostWeiboRequest request);

    /**
     * [Create]
     * Description: 转发
     * <br/> Date: 2020/3/5 23:44
     * <br/>
     */
    PostWeiboResponse rePost(RePostWeiboRequest request);

    /**
     * [Create]
     * Description: 获取微博列表
     * <br/> Date: 2020/2/24 13:53
     * <br/>
     * @param request 列表请求
     */
    WeiBoListResponse list(WeiBoListRequest request);

    /**
     * 查找微博
     */
    WeiBoListResponse search(WeiBoSearchRequest request);

    /**
     * 用户收藏列表
     */
    WeiBoListResponse userCollect(WeiBoSearchRequest request);

    /**
     * 删除
     */
    Boolean delete(WeiBoActionRequest request);

    /**
     * 收藏
     */
    Integer collect(WeiBoActionRequest request);

    /**
     * 取消收藏
     */
    Integer cancelCollect(WeiBoActionRequest request);

    /**
     * 点赞
     */
    Integer likes(WeiBoActionRequest request);

    /**
     * 取消点赞
     */
    Integer cancelLikes(WeiBoActionRequest request);

}
