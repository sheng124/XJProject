package com.xjdzy.service;

import com.xjdzy.dto.ArticleDetailDto;
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

    List<Article>  getAllArticles();

    List<Article>  getAllArticlesByCategoryId(Integer categoryId);

    ArticleDetailDto getArticlesDetailByArticleId(Integer articleId);
}
