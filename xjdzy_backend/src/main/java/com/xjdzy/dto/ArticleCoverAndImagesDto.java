package com.xjdzy.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发布笔记功能所需的返回数据传送对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCoverAndImagesDto {

    private Integer articleId;
    private String articleCover;
    private List<String> articleImages;

}
