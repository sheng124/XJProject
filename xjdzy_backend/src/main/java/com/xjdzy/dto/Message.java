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

    private String content;
    private boolean isRead;

    /**
     * 两个注解用于LocalDateTime的序列化
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime sendTime;

    /**
     * code为1表明该消息是用户发送的消息，content为消息内容；
     * code为0表明该消息是服务器发送的控制消息，content为一个用户ID，表明发给该用户的消息已读
     */
    private int code;

    /**
     * 消息内容类型：1表示图片，content为图片URL；0表示文本，content为文本内容
     */
    private int type;
}
