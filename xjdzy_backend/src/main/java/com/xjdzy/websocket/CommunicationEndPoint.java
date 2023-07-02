package com.xjdzy.websocket;

import com.xjdzy.dto.Message;
import com.xjdzy.entity.ChatRecords;
import com.xjdzy.mapper.ChatRecordsMapper;
import com.xjdzy.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通信站点，为每一个客户端创建一个
 */

@ServerEndpoint("/chat/{userId}")
@Component
public class CommunicationEndPoint {

    // 存储每一个客户端对应的CommunicationEndPoint对象
    private static Map<Integer,CommunicationEndPoint> communicationUsers = new ConcurrentHashMap<>();

    // session对象，通过session对象发送消息给用户
    private Session session;

    // 用户ID
    private Integer userId;

    // ChatRecordsMapper，用于存储记录(不能自动注入)
    private ChatRecordsMapper chatRecordsMapper;

    // 解决注入mapper问题
    private static ApplicationContext applicationContext;
    public static void setApplicationContext(ApplicationContext context) {
        CommunicationEndPoint.applicationContext = context;
    }

    /**
     * 发送聊天记录
     * @param allMessageList 聊天记录
     */
    private void sendChatRecords(List<ChatRecords> allMessageList){
        for(ChatRecords chatRecords:allMessageList){
            Message message = Message.builder()
                    .content(chatRecords.getContent())
                    .sendTime(chatRecords.getSendTime())
                    .isRead(chatRecords.isRead())
                    .contentType(chatRecords.getContentType())
                    .build();
            if(chatRecords.getToUserId().equals(this.userId)){
                message.setUserId(chatRecords.getFromUserId());
                message.setCode(2);
            }
            else{
                message.setUserId(chatRecords.getToUserId());
                message.setCode(1);
            }
            try {
                this.session.getBasicRemote().sendText(JsonUtils.objectToJSONString(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将接收到的消息状态改为已读
     */
    private void setReadStatus(){
        // 检索向该用户发送消息的用户的userId
        List<Integer> userList = chatRecordsMapper.getFromUserIdByToUserId(this.userId);
        // 修改数据库中消息状态
        chatRecordsMapper.setIsReadByUserId(this.userId);
        // 判断消息的发送方是否在线，若在线需要立即向其反馈
        for(Integer uId:userList){
            if(communicationUsers.containsKey(uId)){
                try {
                    Message systemMsg = Message.builder()
                            .code(0)
                            .userId(this.userId)
                            .build();
                    communicationUsers.get(uId).session.getBasicRemote().sendText(JsonUtils.objectToJSONString(systemMsg));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 连接成功时执行
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig, @PathParam("userId") Integer userId){
        // 初始化通信站点信息
        this.session = session;
        this.userId = userId;
        this.chatRecordsMapper = applicationContext.getBean(ChatRecordsMapper.class);
        // 输出上线提示
        System.out.println("用户 "+this.userId+" 已上线！");
        // 将当前通信站点存入站点集合
        communicationUsers.put(this.userId,this);
        // 查看是否有聊天记录，如果有，将聊天记录发送给该用户
        List<ChatRecords> allMessageList = chatRecordsMapper.getAllMessageByUserId(this.userId);
        sendChatRecords(allMessageList);
        // 发送完毕后将未读消息改为已读
        setReadStatus();
    }

    // 接收到消息时执行
    @OnMessage
    public void OnMessage(String message,Session session){
        // 获取接受方ID和消息内容
        Message messageObj1 = JsonUtils.JSONStringToMessage(message);
        System.out.println("收到消息："+messageObj1);
        if (messageObj1 != null) {
            Integer toUserId = messageObj1.getUserId();
            String content = messageObj1.getContent();
            LocalDateTime sendTime = LocalDateTime.now();
            // 查看对方是否在线
            if(communicationUsers.containsKey(toUserId)){
                // 对方在线，构造并发送发送Message
                try {
                    Message messageObj2 = Message.builder()
                            .code(2)
                            .userId(this.userId)
                            .content(content)
                            .contentType(0)
                            .sendTime(sendTime)
                            .isRead(true)
                            .build();
                    communicationUsers.get(toUserId).session.getBasicRemote().sendText(JsonUtils.objectToJSONString(messageObj2));
                    // 告知发送方，消息已读
                    Message systemMsg = Message.builder()
                            .code(0)
                            .userId(toUserId)
                            .build();
                    communicationUsers.get(this.userId).session.getBasicRemote().sendText(JsonUtils.objectToJSONString(systemMsg));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 构造ChatRecords，将聊天记录存入数据库，标记为已读
                ChatRecords chatRecords = ChatRecords.builder()
                        .toUserId(toUserId)
                        .fromUserId(this.userId)
                        .content(content)
                        .isRead(true)
                        .sendTime(sendTime)
                        .build();
                chatRecordsMapper.insert(chatRecords);
            }
            else{
                // 对方不在线，构造ChatRecords，将聊天记录存入数据库，标记为未读
                ChatRecords chatRecords = ChatRecords.builder()
                        .toUserId(toUserId)
                        .fromUserId(this.userId)
                        .content(content)
                        .isRead(false)
                        .sendTime(sendTime)
                        .build();
                chatRecordsMapper.insert(chatRecords);
            }
        }
    }

    // 连接关闭时执行
    @OnClose
    public void OnClose(Session session){
        // 移除当前通信站点
        communicationUsers.remove(this.userId);
        System.out.println("用户 "+this.userId+" 已下线！");
    }

    // 发送错误时执行
    @OnError
    public void OnError(Session session, Throwable throwable){
        // 报错
        System.out.println("websocket模块错误！");
    }
}
