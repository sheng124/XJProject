package com.xjdzy.dto;

import com.xjdzy.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 获取笔记详细信息功能所需的数据传送对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailDto {
    // Article
    private Integer articleId;
    private String articleTitle;
    private String articleCover;
    private String articleContent;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private int viewsNum;
    // Category
    private String categoryName;
    // RelArticleTag
    private List<Tag> tagList;
    // 从Likes统计
    private int likesNum;
    // 从Collection统计
    private int collectionNum;
    // 从Comment统计
    private int commentNum;
    // 从UserInfo和Comment统计
    private List<CAndUDto> cAndUDtoList;
    // 从ArticleImages中获取
    List<String> articleImages;
}
