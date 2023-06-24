package com.xjdzy.dto;


import com.xjdzy.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 发布笔记功能所需的数据传送对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleWriteAndUpdateDto {
    // Article
    private int articleId;
    private int userId;
    private String articleTitle;
    private String articleCover;
    private String articleContent;
    private int categoryId;
    private LocalDateTime createTime;
    // RelArticleTag
    private List<Tag> tagList;
}
