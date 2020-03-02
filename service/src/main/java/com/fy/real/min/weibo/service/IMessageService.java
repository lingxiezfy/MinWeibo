package com.fy.real.min.weibo.service;

import com.fy.real.min.weibo.model.message.SendGroupMessageRequest;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/2 18:37 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
public interface IMessageService {
    Boolean sendGroup(SendGroupMessageRequest request);
}
