package com.xjdzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjdzy.dto.ArticleSummaryDto;
import com.xjdzy.entity.Article;
import com.xjdzy.entity.Category;
import com.xjdzy.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    int getLikesNumByUserId(Integer userId);

    int getLikesNumByArticleId(Integer articleId);

    int getCollectionNumByUserId(Integer userId);

    int getCollectionNumByArticleId(Integer articleId);

    List<ArticleSummaryDto> getAllByArticleIdArticleSummaryDtos(Integer articleId);

    List<Integer> getAllArticleId();

    List<Integer> getArticleIdByCategoryId(Integer categoryId);

    List<Integer> getArticleIdByTagId(Integer tagId);

    List<Integer> getArticleIdByWrite(Integer userId);

    List<Integer> getArticleIdByCollection(Integer userId);

    List<Integer> getArticleIdByLike(Integer userId);

    List<String> getArticleImageByArticleId(Integer articleId);

    List<Category> getCategoryByArticleId(Integer articleId);

    List<UserInfo> getUserInfoByArticleId(Integer articleId);

    boolean updateViewsNumByArticleId(Integer articleId);

}
