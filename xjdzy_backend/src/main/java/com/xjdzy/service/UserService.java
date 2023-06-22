package com.xjdzy.service;

import com.xjdzy.dto.ArticleTypeDto;
import com.xjdzy.dto.ArticleWriteAndUpdateDto;
import com.xjdzy.dto.ArticlesDataDto;
import com.xjdzy.dto.FoFoLiCoNumDto;
import com.xjdzy.entity.Follow;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    boolean followService(Follow follow);

    boolean unFollowService(Follow follow);

    FoFoLiCoNumDto getFoFoLiCoNumService(Integer userId);

    List<ArticleTypeDto> getWrLiCoArticlesService(Integer userId);

    Integer writeArticleService(ArticleWriteAndUpdateDto articleWriteAndUpdateDto);

    boolean updateArticleService(ArticleWriteAndUpdateDto articleWriteAndUpdateDto);

    boolean deleteArticleService(Integer articleId);

    ArticlesDataDto getAllArticleDataService(Integer userId);

    String updateUserAvatarService(MultipartFile imageFile,String userName);
}
