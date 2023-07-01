package com.xjdzy.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRecords {
    @TableId(type = IdType.AUTO)
    private Integer chatRecordsId;

    private Integer toUserId;
    private Integer fromUserId;
    private String content;
    private boolean isRead;
    private LocalDateTime sendTime;

}
