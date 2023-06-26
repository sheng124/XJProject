package com.xjdzy.service;

import com.xjdzy.dto.ArticleDetailDto;
import com.xjdzy.dto.ArticleSummaryDto;
import com.xjdzy.entity.Article;
import com.xjdzy.entity.Collection;
import com.xjdzy.entity.Comment;
import com.xjdzy.entity.Likes;

import java.util.List;

public interface ArticlesService {

    boolean likesService(Likes likes);

    boolean unLikesService(Likes likes);

    boolean collectionService(Collection collection);

    boolean unCollectionService(Collection collection);

    Integer commentService(Comment comment);

    boolean unCommentService(Integer commentId);

    List<ArticleSummaryDto>  getAllArticles();

    List<ArticleSummaryDto>  getAllArticlesByCategoryId(Integer categoryId);

    ArticleDetailDto getArticlesDataByArticleId(Integer articleId,ArticleDetailDto articleDetailDto);

    ArticleDetailDto getArticlesDetailByArticleId(Integer articleId);

    List<ArticleSummaryDto> getArticleSummaryByArticleId(List<Integer> articleIdList);
}
