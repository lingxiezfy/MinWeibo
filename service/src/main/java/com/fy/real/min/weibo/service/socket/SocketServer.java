package com.fy.real.min.weibo.service.socket;

import com.alibaba.fastjson.JSON;
import com.fy.real.min.weibo.dao.dao.UserDao;
import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.enums.MessageTypeEnum;
import com.fy.real.min.weibo.model.message.MessageBase;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@ServerEndpoint(value = "/ws/server")
@Component
public class SocketServer {

    private static UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        SocketServer.userDao = userDao;
    }

    @PostConstruct
    public void init() {
        System.out.println("websocket 加载");
    }
    private static final Logger log = LoggerFactory.getLogger(SocketServer.class);
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<Session>();
    // 组内在线用户 （-1 微博消息组；1-n 讨论组用户）
    private static ConcurrentHashMap<Integer, CopyOnWriteArraySet<Session>> groupSessionMap = new ConcurrentHashMap<>();

    // 用户持有的微博消息Session列表
    private static ConcurrentHashMap<Integer, CopyOnWriteArraySet<Session>> userWeiBoSessionMap = new ConcurrentHashMap<>();

    // session 与用户绑定 （用于快速定位Session指向的用户）
    private static ConcurrentHashMap<Session, User> sessionUserMap = new ConcurrentHashMap<>();

    // session 与组绑定（用于快速定位Session指向的组）
    private static ConcurrentHashMap<Session, Integer> sessionGroupMap = new ConcurrentHashMap<>();


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        SessionSet.add(session);
        int cnt = OnlineCount.incrementAndGet(); // 在线数加1
        log.info("有连接加入，当前连接数为：{}", cnt);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        Integer groupId = sessionGroupMap.remove(session);
        User user = sessionUserMap.remove(session);;
        if(groupId != null){
            CopyOnWriteArraySet<Session> set = groupSessionMap.get(groupId);
            if(set != null){
                set.remove(session);
            }
            if(user != null){
                if(groupId == -1){
                    CopyOnWriteArraySet<Session> userWeiBoSession =  userWeiBoSessionMap.get(user.getUserId());
                    if (userWeiBoSession != null) {
                        userWeiBoSession.remove(session);
                    }
                }else if(groupId > 0){
                    adminMessageToChatGroup(groupId,"用户:<span style='color:green;'>"+user.getNickname()+"</span> 退出了讨论");
                }
            }
        }

        SessionSet.remove(session);
        int cnt = OnlineCount.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     * group;{-1,groupId};{in,message};{userId,something to say}
     *
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息：{}",message);
        String[] arr = message.split(";",4);
        try {
            if (arr.length == 4) {
                switch (arr[0]) {
                    case "group"://组消息
                        Integer groupId = Integer.parseInt(arr[1]);
                        switch (arr[2]) {
                            case "in": // 用户绑定 group;{groupId};in;{userId}
                                Integer userId = Integer.parseInt(arr[3]);
                                User user = userDao.selectByPrimaryKey(userId);
                                if(user != null){
                                    sessionGroupMap.computeIfAbsent(session,k->groupId);
                                    groupSessionMap.computeIfAbsent(groupId,k->new CopyOnWriteArraySet<>()).add(session);
                                    sessionUserMap.computeIfAbsent(session,k->user);
                                    if(groupId > 0){
                                        adminMessageToChatGroup(groupId,"欢迎<span style='color:red;'>"+user.getNickname()+"</span>加入");
                                    }else if(groupId == -1){
                                        userWeiBoSessionMap.computeIfAbsent(userId,k->new CopyOnWriteArraySet<>()).add(session);
                                    }
                                    log.info("用户{}:{}注册加入组{}",user.getNickname(),user.getUserId(),groupId);
                                }else {
                                    log.error("用户注册失败，没找到该用户");
                                }
                                break;
                            case "msg": //组内群发消息 group;{groupId};msg;{something to say}
                                String content = arr[3];
                                User fromUser = sessionUserMap.get(session);
                                if(fromUser == null){
                                    log.error("发送消息失败，该用户未注册组");
                                }else {
                                    if(groupId > 0){
                                        sendToChatGroup(fromUser,groupId,content);
                                    }else {
                                        // 发送微博通知
                                        sendWeiBoSystemNotice(fromUser,content,null);
                                    }

                                    log.info("用户{}:{}向组{}群发{}",fromUser.getNickname(),fromUser.getUserId(),groupId,content);
                                }
                                break;
                            default:
                                log.warn("未解析消息{}",message);
                        }
                        break;
                    default:
                        log.warn("未解析消息{}",message);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 发布微博通知
     */
    private static void sendWeiBoSystemNotice(User fromUser, String content,Integer messageId) {
        CopyOnWriteArraySet<Session> groupSessions = groupSessionMap.get(-1);
        if(groupSessions != null && groupSessions.size() > 0 && StringUtils.isNotBlank(content)){
            String message = JSON.toJSONString(new MessageBase(fromUser, MessageTypeEnum.SystemNotice.name(),content,new Date(),messageId));
            for(Session session : groupSessions){
                if(session.isOpen()){
                    SendMessage(session,message);
                }
            }
        }
    }

    public static void sendMessageToUser(User fromUser,Integer toId,MessageTypeEnum messageType,String content,Integer messageId){
        if (MessageTypeEnum.SystemNotice.equals(messageType)) {
            SocketServer.sendWeiBoSystemNotice(fromUser,content,messageId);
        }else {
            CopyOnWriteArraySet<Session> userSessions = SocketServer.userWeiBoSessionMap.get(toId);
            if(userSessions != null && userSessions.size() > 0 && StringUtils.isNotBlank(content)){
                String message = JSON.toJSONString(new MessageBase(fromUser, messageType.name(),content,new Date(),messageId));
                for(Session session : userSessions){
                    if(session.isOpen()){
                        SendMessage(session,message);
                    }
                }
            }else {
                log.error("发送消息失败,用户:{}不在线,消息:{},发送方:{}",toId,content,fromUser.getNickname());
            }
        }

    }


    /**
     * 管理员发布讨论组消息
     */
    private void adminMessageToChatGroup(Integer groupId, String content ){
        User admin = new User();
        admin.setAdminAble(1);
        admin.setUsername("System");
        admin.setNickname("系统");
        admin.setUserId(-1);
        sendToChatGroup(admin,groupId,content);
    }

    /**
     * 讨论组内群发
     */
    private void sendToChatGroup(User user,Integer groupId, String content) {
        if(groupId <= 0){
            log.error("发送消息失败，{}不属于讨论组",groupId);
            return;
        }
        CopyOnWriteArraySet<Session> groupSessions = groupSessionMap.get(groupId);
        if(groupSessions != null && groupSessions.size() > 0 && StringUtils.isNotBlank(content)){
            String message = JSON.toJSONString(new MessageBase(user, MessageTypeEnum.ChatMessage.name(),content,new Date()));
            for(Session session : groupSessions){
                if(session.isOpen()){
                    SendMessage(session,message);
                }
            }
        }
    }

    /**
     * 出现错误
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}",error.getMessage(),session.getId());
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     */
    public static void SendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息出错", e);
        }
    }

    /**
     * 群发消息
     * @param message
     * @throws IOException
     */
    public static void BroadCastInfo(String message) throws IOException {
        for (Session session : SessionSet) {
            if(session.isOpen()){
                SendMessage(session, message);
            }
        }
    }

    /**
     * 指定Session发送消息
     * @param sessionId
     * @param message
     * @throws IOException
     */
    public static void SendMessage(String message,String sessionId) throws IOException {
        Session session = null;
        for (Session s : SessionSet) {
            if(s.getId().equals(sessionId)){
                session = s;
                break;
            }
        }
        if(session!=null){
            SendMessage(session, message);
        }
        else{
            log.warn("没有找到你指定ID的会话：{}",sessionId);
        }
    }

}
