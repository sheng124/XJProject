package com.xjdzy.dto;


import com.xjdzy.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 发布笔记功能所需的接收数据传送对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleWriteAndUpdateDto {
    // Article
    private int articleId;
    private int userId;
    private String articleTitle;
    private String articleContent;
    private String articleCover;
    private int categoryId;
    private LocalDateTime createTime;
    private String videoUrl;
    // RelArticleTag
    private List<Tag> tagList;
    // ArticleImages
    private List<String> imageList;
}
