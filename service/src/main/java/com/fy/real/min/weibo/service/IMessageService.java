package com.fy.real.min.weibo.service;

import com.fy.real.min.weibo.model.message.SendGroupMessageRequest;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
public interface IMessageService {
    Boolean sendGroup(SendGroupMessageRequest request);
}
