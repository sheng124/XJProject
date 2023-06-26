package com.xjdzy.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer commentId;

    private String comment;
    private LocalDateTime createTime;
    private int articleId;
    private int userId;
}
