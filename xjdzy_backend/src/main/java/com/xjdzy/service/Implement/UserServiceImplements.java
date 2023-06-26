package com.xjdzy.service.Implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xjdzy.dto.*;
import com.xjdzy.entity.*;
import com.xjdzy.mapper.*;
import com.xjdzy.service.ArticlesService;
import com.xjdzy.service.RedisService;
import com.xjdzy.service.UserService;
import com.xjdzy.utils.ImageToBase64Utils;
import com.xjdzy.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImplements implements UserService {

    /**
     * 注入需要的Mapper或Service
     */
    @Autowired(required = false)
    FollowMapper followMapper;

    @Autowired(required = false)
    ArticleMapper articleMapper;

    @Autowired(required = false)
    RelArticleTagMapper relArticleTagMapper;

    @Autowired(required = false)
    UserMapper userMapper;

    @Autowired(required = false)
    CategoryMapper categoryMapper;

    @Autowired(required = false)
    ArticleImagesMapper articleImagesMapper;

    @Autowired
    private ArticlesService articlesService;

    @Autowired
    private RedisService redisService;



    /**
     * 关注
     * @param follow 关注实体类
     * @return 关注成功true,关注失败false
     */
    @Override
    public boolean followService(Follow follow) {
        return followMapper.insert(follow) == 1;
    }

    /**
     * 取消关注
     * @param follow 关注实体类
     * @return 取消关注成功true,取消关注失败false
     */
    @Override
    public boolean unFollowService(Follow follow) {
        Map<String,Object> map=new HashMap<>();
        map.put("user_id",follow.getUserId());
        map.put("following_user_id",follow.getFollowingUserId());
        return followMapper.deleteByMap(map) == 1;
    }

    /**
     * 获取用户关注数、粉丝数、获赞与收藏数
     * @param userId 用户ID
     * @return 数据传送对象FoFoLiCoNumDto
     */
    @Override
    public FoFoLiCoNumDto getFoFoLiCoNumService(Integer userId){
        // 构造空的FoFoLiCoNumDto
        FoFoLiCoNumDto foFoLiCoNumDto=new FoFoLiCoNumDto();
        // 统计关注数
        LambdaQueryWrapper<Follow> lqw1=new LambdaQueryWrapper<>();
        lqw1.eq(Follow::getUserId,userId);
        int tmpFCount1 = Math.toIntExact(followMapper.selectCount(lqw1));
        foFoLiCoNumDto.setFollowing(tmpFCount1);
        // 统计粉丝数
        LambdaQueryWrapper<Follow> lqw2=new LambdaQueryWrapper<>();
        lqw2.eq(Follow::getFollowingUserId,userId);
        int tmpFCount2 = Math.toIntExact(followMapper.selectCount(lqw2));
        foFoLiCoNumDto.setFollowers(tmpFCount2);
        // 统计获赞数
        int tmpLCount = articleMapper.getLikesNumByUserId(userId);
        foFoLiCoNumDto.setLikesNum(tmpLCount);
        // 统计收藏数
        int tmpCCount = articleMapper.getCollectionNumByUserId(userId);
        foFoLiCoNumDto.setCollectionNum(tmpCCount);
        // 返回结果
        return foFoLiCoNumDto;
    }


    /**
     * 获取已发布、已收藏、已点赞笔记
     * @param userId 用户ID
     * @return 数据传送对象ArticleTypeDto
     */
    @Override
    public List<ArticleSummaryDto> getWrLiCoArticlesService(Integer userId) {
        // 0.创建空的ArticleTypeDtoList
        List<ArticleSummaryDto> articleSummaryDtoListAll = new ArrayList<>();
        // 1.查询已发布笔记的相关数据以及categoryName：type为1
        List<Integer> articleIdList1 = articleMapper.getArticleIdByWrite(userId);
        List<ArticleSummaryDto> articleSummaryDtoList1 = articlesService.getArticleSummaryByArticleId(articleIdList1);
        int listLength1= articleSummaryDtoList1.size();
        // 2.查询已收藏笔记的相关数据以及categoryName：type为2
        List<Integer> articleIdList2 = articleMapper.getArticleIdByCollection(userId);
        List<ArticleSummaryDto> articleSummaryDtoList2 = articlesService.getArticleSummaryByArticleId(articleIdList2);
        int listLength2= articleSummaryDtoList2.size()+listLength1;
        // 3.查询已点赞笔记的相关数据以及categoryName：type为3
        List<Integer> articleIdList3 = articleMapper.getArticleIdByLike(userId);
        List<ArticleSummaryDto> articleSummaryDtoList3 = articlesService.getArticleSummaryByArticleId(articleIdList3);
        int listLength3= articleSummaryDtoList3.size()+listLength2;
        // 4.遍历每一篇笔记
        articleSummaryDtoListAll.addAll(articleSummaryDtoList1);
        articleSummaryDtoListAll.addAll(articleSummaryDtoList2);
        articleSummaryDtoListAll.addAll(articleSummaryDtoList3);
        int n=0;
        for(ArticleSummaryDto articleSummaryDto : articleSummaryDtoListAll){
            // 4.1设置type
            if(n<listLength1)
                articleSummaryDto.setType(1);
            else if(n<listLength2)
                articleSummaryDto.setType(2);
            else articleSummaryDto.setType(3);
            n++;
        }
        // 5.返回结果
        return articleSummaryDtoListAll;
    }

    /**
     * 发布笔记和修改笔记使用：插入笔记图片
     * @param articleImageList 图片列表，MultipartFile形式
     * @param articleId 笔记ID
     * @return 图片列表，Base64格式
     */
    public List<String> addArticleImages(MultipartFile[] articleImageList,Integer articleId){
        List<String> imageBaseList = new ArrayList<>();
        for(MultipartFile file:articleImageList){
            String tmpBase64 = ImageToBase64Utils.ImageToBase64(file);
            ArticleImages articleImages = ArticleImages.builder().articleId(articleId).image(tmpBase64).build();
            imageBaseList.add(tmpBase64);
            if(articleImagesMapper.insert(articleImages) != 1)
                return null;
        }
        return imageBaseList;
    }

    /**
     * 发布笔记
     * @param articleWriteAndUpdateDto 数据传送实体类ArticleWriteDto
     * @return 成功为生成的articleId,失败为null
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean writeArticleService(ArticleWriteAndUpdateDto articleWriteAndUpdateDto,
                                                        MultipartFile articleCover,
                                                        MultipartFile[] articleImageList) {
        // 封面图片编码
        String articleCoverBase = ImageToBase64Utils.ImageToBase64(articleCover);
        // 更新Article表
        Article article = Article.builder().articleTitle(articleWriteAndUpdateDto.getArticleTitle())
                .articleCover(articleCoverBase)
                .articleContent(articleWriteAndUpdateDto.getArticleContent())
                .categoryId(articleWriteAndUpdateDto.getCategoryId())
                .createTime(articleWriteAndUpdateDto.getCreateTime())
                .updateTime(articleWriteAndUpdateDto.getCreateTime())
                .userId(articleWriteAndUpdateDto.getUserId())
                .build();
        if(articleMapper.insert(article) != 1)
            return false;
        else{
            // 更新RelArticleAndTag表
            Integer articleId=article.getArticleId();
            for(Tag tag: articleWriteAndUpdateDto.getTagList()){
                RelArticleTag relArticleTag=new RelArticleTag();
                relArticleTag.setArticleId(articleId);
                relArticleTag.setTagId(tag.getTagId());
                if(relArticleTagMapper.insert(relArticleTag) != 1)
                    return false;
            }
            // 更新ArticleImages表
            addArticleImages(articleImageList,articleId);
            return true;
        }
    }

    /**
     * 修改笔记
     * @param articleWriteAndUpdateDto 数据传送实体类ArticleWriteDto
     * @return 成功为true,失败为false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateArticleService(ArticleWriteAndUpdateDto articleWriteAndUpdateDto,
                                                         MultipartFile articleCover,
                                                         MultipartFile[] articleImageList) {
        // 封面图片编码
        String articleCoverBase = ImageToBase64Utils.ImageToBase64(articleCover);
        // 更新Article表
        Article article=Article.builder().articleId(articleWriteAndUpdateDto.getArticleId())
                .articleTitle(articleWriteAndUpdateDto.getArticleTitle())
                .articleCover(articleCoverBase)
                .articleContent(articleWriteAndUpdateDto.getArticleContent())
                .categoryId(articleWriteAndUpdateDto.getCategoryId())
                .createTime(articleWriteAndUpdateDto.getCreateTime())
                .updateTime(articleWriteAndUpdateDto.getCreateTime())
                .userId(articleWriteAndUpdateDto.getUserId())
                .build();
        if(articleMapper.updateById(article) != 1)
            return false;
        else{
            Integer articleId=article.getArticleId();
            // 将原有标签删除
            Map<String,Object> map1 = new HashMap<>();
            map1.put("article_id",articleId);
            relArticleTagMapper.deleteByMap(map1);
            // 加入新的标签
            for(Tag tag: articleWriteAndUpdateDto.getTagList()){
                RelArticleTag relArticleTag=new RelArticleTag();
                relArticleTag.setArticleId(articleId);
                relArticleTag.setTagId(tag.getTagId());
                if(relArticleTagMapper.insert(relArticleTag) != 1)
                    return false;
            }
            // 将原有图片删除
            Map<String,Object> map2 = new HashMap<>();
            map2.put("article_id",articleId);
            articleImagesMapper.deleteByMap(map2);
            // 加入新的图片
            addArticleImages(articleImageList,articleId);
            return true;
        }
    }

    /**
     * 删除笔记
     * @param articleId 笔记ID
     * @return 成功为true,失败为false
     */
    @Override
    public boolean deleteArticleService(Integer articleId) {
        Map<String,Object> map = new HashMap<>();
        map.put("article_id",articleId);
        articleMapper.deleteByMap(map);
        return true;
    }

    /**
     * 获取用户所有笔记相关数据
     * @param userId 用户ID
     * @return 数据传送对象ArticlesDataDto
     */
    @Override
    public ArticlesDataDto getAllArticleDataService(Integer userId) {
        // 获取用户已发布笔记的articleId
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.select(Article::getArticleId).eq(Article::getUserId,userId);
        List<Object> articleIdList = articleMapper.selectObjs(lqw);
        // 获取每一篇笔记的数据并求和
        int viewsNum=0;
        int likesNum=0;
        int collectionNum=0;
        int commentNum=0;
        List<ArticleDetailDto> articleDetailDtoList=new ArrayList<>();
        for(Object articleId:articleIdList){
            ArticleDetailDto tmpArticleDetailDto = new ArticleDetailDto();
            articlesService.getArticlesDataByArticleId((Integer) articleId,tmpArticleDetailDto);
            viewsNum+=tmpArticleDetailDto.getViewsNum();
            likesNum+=tmpArticleDetailDto.getLikesNum();
            collectionNum+=tmpArticleDetailDto.getCollectionNum();
            commentNum+=tmpArticleDetailDto.getCommentNum();
            articleDetailDtoList.add(tmpArticleDetailDto);
        }
        // 构造ArticlesDataDto并返回
        return new ArticlesDataDto(viewsNum,likesNum,collectionNum,commentNum,articleDetailDtoList);
    }

    /**
     * 上传用户头像
     * @param imageFile 头像文件
     * @param userId 用户ID
     * @return 图片转换为base64格式的字符串编码
     */
    @Override
    @Transactional
    public String updateUserAvatarService(MultipartFile imageFile, Integer userId) {
        String imageBase64 = ImageToBase64Utils.ImageToBase64(imageFile);
        if(imageBase64 != null && !imageBase64.equals("0") && !imageBase64.equals("1") && !imageBase64.equals("2")){
            LambdaUpdateWrapper<UserInfo> luw=new LambdaUpdateWrapper<>();
            luw.set(UserInfo::getUserAvatar,imageBase64)
                    .eq(UserInfo::getUserId,userId);
            userMapper.update(null,luw);
        }
        redisService.deleteUserInfo(userId);
        return imageBase64;
    }
    /**
     * 获取所有类别
     * @param
     * @param
     * @return
     */
    @Override
    public List<Category> getCateGoriesService() {
        return categoryMapper.getCateGoriesCon();
    }

    /**
     * 修改密码
     * @param
     * @param
     * @return
     */
    @Override
    @Transactional
    public boolean updatePasswordService(UpdatePasswordDto updatePasswordDto) {
       List<UserInfo> u=userMapper.CheckisR(updatePasswordDto.getUserId(),updatePasswordDto.getPassword());
       if(u.size()==0)
           return false;
       userMapper.updatePasswordCon(updatePasswordDto.getUserId(),updatePasswordDto.getNewPassword());
       redisService.deleteUserInfo(updatePasswordDto.getUserId());
       return true;

    }

    /**
     * 修改用户名
     * @param
     * @param
     * @return
     */
    @Transactional
    @Override
    public boolean updateUserNameService(UpdateUserNameDto updateUserNameDto) {
        List<UserInfo> a = userMapper.CheckisR2(updateUserNameDto.getNewUserName());
        if(a.size()!=0)
            return false;
        userMapper.updateUserNameCon(updateUserNameDto.getUserId(),updateUserNameDto.getNewUserName());
        redisService.deleteUserInfo(updateUserNameDto.getUserId());
        return true;
    }

    /**
     * 获取用户信息
     * @param
     * @param
     * @return
     */
    @Override
    public UserInfo getUserInfoService(Integer userId) {
        String userIdStr = userId.toString();
        // 首先在Redis中查询用户信息是否存在
        String value = redisService.getUserInfo(userIdStr);
        if(value == null) {
            // 若不存在，查询数据库，将用户信息写入Redis并返回
            log.info("Redis中没有查询到用户信息！");
            List<UserInfo> userInfoList = userMapper.getUserInfoCon(userIdStr);
            if (userInfoList.size() == 0) {
                redisService.saveUserInfo(userIdStr, null);
                return null;
            } else {
                redisService.saveUserInfo(userIdStr, userInfoList.get(0));
                return userInfoList.get(0);
            }
        }
        else if(value.equals(""))
            // 若存在，但是无内容，则返回null
            return null;
        else
            return JSONUtils.JSONStringToUserInfo(value);
    }

}
