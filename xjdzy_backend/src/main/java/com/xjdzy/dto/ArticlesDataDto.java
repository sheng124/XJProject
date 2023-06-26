package com.xjdzy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 获取用户所有笔记相关数据功能所需的数据传送对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticlesDataDto {
    private int viewsNum;
    private int likesNum;
    private int collectionNum;
    private int commentNum;
    List<ArticleDetailDto> articleDetailDtoList;
}
