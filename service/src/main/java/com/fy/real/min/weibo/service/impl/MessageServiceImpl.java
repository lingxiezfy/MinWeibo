package com.fy.real.min.weibo.service.impl;

import com.fy.real.min.weibo.dao.dao.MessageDao;
import com.fy.real.min.weibo.dao.dao.UserDao;
import com.fy.real.min.weibo.model.entity.Message;
import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.enums.MessageTypeEnum;
import com.fy.real.min.weibo.model.exception.WeiBoException;
import com.fy.real.min.weibo.model.message.MessageListResponse;
import com.fy.real.min.weibo.model.message.MessageViewItem;
import com.fy.real.min.weibo.model.message.QueryMessageRequest;
import com.fy.real.min.weibo.model.message.SystemNoticeRequest;
import com.fy.real.min.weibo.model.user.UserView;
import com.fy.real.min.weibo.service.IMessageService;
import com.fy.real.min.weibo.service.socket.SocketServer;
import com.fy.real.min.weibo.util.utils.ValidatorUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@Service
public class MessageServiceImpl implements IMessageService {

    @Autowired
    MessageDao messageDao;

    @Autowired
    UserDao userDao;

    @Override
    public Boolean addMessage(User from, User to, MessageTypeEnum messageType, String messageContent, Integer otherId,boolean notice) {
        if(messageType == null){
            return false;
        }
        Message message = new Message();
        message.setFromId(from.getUserId());
        message.setToId(to == null ? 0 : to.getUserId());
        message.setMessageType(messageType.getCode());
        message.setMessageContent(StringUtils.isBlank(messageContent) ? messageType.getDesc() : messageContent);
        message.setOriginSource(otherId);
        messageDao.insertSelective(message);
        if(notice){
            SocketServer.sendMessageToUser(from,message.getToId(),messageType,message.getMessageContent(),message.getMessageId());
        }
        return true;
    }

    @Override
    public MessageListResponse query(QueryMessageRequest request) {
        MessageListResponse response = new MessageListResponse();
        Page page = PageHelper.startPage(request.getPageIndex(), request.getPageSize());
        PageHelper.orderBy("message_type,read_state,create_time desc");
        Message queryMessage = new Message();
        queryMessage.setToId(request.getCurrentUser().getUserId());
        if(request.getMessageType() != null && request.getMessageType() > 0){
            queryMessage.setMessageType(request.getMessageType());
        }
        if(request.getMessageId() != null && request.getMessageId() > 0){
            queryMessage.setMessageId(request.getMessageId());
        }
        List<Message> messages = messageDao.query(queryMessage);
        if(messages.size() > 0){
            response.setPageIndex(page.getPageNum());
            response.setTotalCount(page.getTotal());
            response.setTotalPage(page.getPages());
            User currentUser = request.getCurrentUser();
            List<User> formUses = userDao.selectByUserIdList(messages.stream().map(Message::getFromId).collect(Collectors.toList()));
            List<MessageViewItem> viewList = new ArrayList<>();
            messages.forEach(message -> {
                MessageViewItem viewItem = new MessageViewItem();
                BeanUtils.copyProperties(message,viewItem);
                User fromUser = formUses.stream().filter(user -> user.getUserId().equals(message.getFromId())).findFirst().orElse(null);
                viewItem.setFromUser(UserView.convertFromUser(fromUser));
                viewItem.setToUser(UserView.convertFromUser(currentUser));
                viewList.add(viewItem);
            });
            response.setList(viewList);
        }else {
            response.setList(new ArrayList<>());
        }
        return response;
    }

    @Override
    public Boolean delete(QueryMessageRequest request) {
        if(request.getMessageId() == null || request.getMessageId() <= 0){
            throw new WeiBoException("必须指定消息进行操作");
        }
        Message message = messageDao.selectByPrimaryKey(request.getMessageId());
        if (message.getToId().equals(request.getCurrentUser().getUserId())
                ||message.getFromId().equals(request.getCurrentUser().getUserId())) {
            messageDao.deleteByPrimaryKey(request.getMessageId());
            return true;
        }
        throw new WeiBoException("无权操作");
    }

    @Override
    public Boolean systemNotice(SystemNoticeRequest request) {
        ValidatorUtil.validate(request);
        if(request.getCurrentUser().getAdminAble() == 0){
            throw new WeiBoException("无权操作");
        }
        addMessage(request.getCurrentUser(),null,MessageTypeEnum.SystemNotice,request.getContent(),null,true);
        return true;
    }
}
