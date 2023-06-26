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
public class Article {
    @TableId(type = IdType.AUTO)
    private Integer articleId;

    private String articleTitle;
    private String articleCover;
    private String articleContent;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private int viewsNum;
    private int userId;
    private int categoryId;
}
