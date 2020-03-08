package com.fy.real.min.weibo.service;

import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.enums.MessageTypeEnum;
import com.fy.real.min.weibo.model.message.MessageListResponse;
import com.fy.real.min.weibo.model.message.QueryMessageRequest;
import com.fy.real.min.weibo.model.message.SystemNoticeRequest;

/**
 * [Create]
 * Description: 消息服务
 * @version 1.0
 */
public interface IMessageService {
    /**
     * 添加消息
     * @param from 发送方
     * @param to 接收方，null或者0均代表 所有用户
     * @param messageType 消息类型 {@link MessageTypeEnum}
     * @param messageContent 消息体，传 null 则使用 messageType 默认的消息体
     * @param otherId 消息关联的id，与各个消息类型有关
     * @param notice 是否进行通知
     */
    Boolean addMessage(User from, User to, MessageTypeEnum messageType,String messageContent,Integer otherId,boolean notice);

    MessageListResponse query(QueryMessageRequest request);

    Boolean delete(QueryMessageRequest request);

    Boolean systemNotice(SystemNoticeRequest request);
}
