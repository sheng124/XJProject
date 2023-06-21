package com.xjdzy.service;

import com.xjdzy.dto.ArticleTypeDto;
import com.xjdzy.dto.FoFoLiCoNumDto;
import com.xjdzy.entity.Follow;

import java.util.List;

public interface UserService {

    boolean followService(Follow follow);

    boolean unFollowService(Follow follow);

    FoFoLiCoNumDto getFoFoLiCoNumService(Integer userId);

    List<ArticleTypeDto> getWrLiCoArticlesService(Integer userId);
}
