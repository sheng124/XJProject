package com.xjdzy.service;

import com.xjdzy.dto.*;
import com.xjdzy.entity.Category;
import com.xjdzy.entity.Follow;
import com.xjdzy.entity.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    boolean followService(Follow follow);

    boolean unFollowService(Follow follow);

    FoFoLiCoNumDto getFoFoLiCoNumService(Integer userId);

    List<ArticleSummaryDto> getWrLiCoArticlesService(Integer userId);

    boolean writeArticleService(ArticleWriteAndUpdateDto articleWriteAndUpdateDto,
                                                 MultipartFile articleCover,
                                                 MultipartFile[] articleImages);

    boolean updateArticleService(ArticleWriteAndUpdateDto articleWriteAndUpdateDto);

    boolean deleteArticleService(Integer articleId);

    ArticlesDataDto getAllArticleDataService(Integer userId);

    String updateUserAvatarService(MultipartFile imageFile,Integer userId);

    List<Category> getCateGoriesService();

    boolean updatePasswordService (UpdatePasswordDto updatePasswordDto);

    boolean updateUserNameService(UpdateUserNameDto updateUserNameDto);

    UserInfo getUserInfoService(Integer userId);

    boolean getFollowStatusService(Integer userId, Integer fUserId);

    List<UserInfo> getFollowingService(Integer userId);

    List<UserInfo> getFollowerService(Integer userId);
}
