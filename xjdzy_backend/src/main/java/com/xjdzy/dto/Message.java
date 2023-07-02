package com.xjdzy.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 私信功能使用
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    /**
     * 发送消息时userId为接收用户ID，接收消息时userId为发送用户ID
     */
    private Integer userId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 是否已读：包括对方是否已读、己方是否已读
     */
    private boolean isRead;

    /**
     * 两个注解用于LocalDateTime的序列化
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime sendTime;

    /**
     * code为0表明该消息是服务器发送的控制消息，表明发给userId的用户的消息已读，其它为null
     * code为1表明该消息是用户发送的消息，content为消息内容，其它为null；
     * code为2表明该消息是用户接收的消息，content为消息内容，其它为null；
     */
    private int code;

    /**
     * 消息内容类型：0表示文本，content为文本内容；1表示图片，content为图片URL
     */
    private int contentType;
}
