package com.xjdzy.dto;

import com.xjdzy.entity.Category;
import com.xjdzy.entity.Tag;
import com.xjdzy.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 获取已发布、已收藏、已点赞文章功能所需的数据传送对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSummaryDto {
    // Article
    private Integer articleId;
    private String articleTitle;
    private String articleCover;
    private String articleContent;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private int viewsNum;
    // Category
    private Category category;
    // RelArticleTag
    private List<Tag> tagList;
    // 从Likes统计
    private int likesNum;
    // 从Collection统计
    private int collectionNum;
    // 自定义
    private int type;
    // UserInfo
    private UserInfo userInfo;
}
