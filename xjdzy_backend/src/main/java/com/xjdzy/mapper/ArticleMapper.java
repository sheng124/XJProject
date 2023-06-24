package com.xjdzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjdzy.dto.ArticleTypeDto;
import com.xjdzy.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    int getLikesNumByUserId(Integer userId);

    int getLikesNumByArticleId(Integer articleId);

    int getCollectionNumByUserId(Integer userId);

    int getCollectionNumByArticleId(Integer articleId);

    List<ArticleTypeDto> getAllByUserIdWrArticles(Integer userId);

    List<ArticleTypeDto> getAllByUserIdCollectionArticles(Integer userId);

    List<ArticleTypeDto> getAllByUserIdLikesArticles(Integer userId);
}
