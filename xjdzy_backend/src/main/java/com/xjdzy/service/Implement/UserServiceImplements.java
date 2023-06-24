package com.xjdzy.service.Implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xjdzy.dto.*;
import com.xjdzy.entity.*;
import com.xjdzy.mapper.*;
import com.xjdzy.service.ArticlesService;
import com.xjdzy.service.UserService;
import com.xjdzy.utils.ImageToBase64Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.util.EnumUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ArticlesService articlesService;



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
    public List<ArticleTypeDto> getWrLiCoArticlesService(Integer userId) {
        // 0.创建空的ArticleTypeDtoList
        List<ArticleTypeDto> articleTypeDtoListAll = new ArrayList<>();
        // 1.查询已发布笔记的相关数据以及categoryName：type为1
        List<ArticleTypeDto> articleTypeDtoList1 = articleMapper.getAllByUserIdWrArticles(userId);
        int listLength1=articleTypeDtoList1.size();
        // 2.查询已收藏笔记的相关数据以及categoryName：type为2
        List<ArticleTypeDto> articleTypeDtoList2 = articleMapper.getAllByUserIdCollectionArticles(userId);
        int listLength2=articleTypeDtoList2.size()+listLength1;
        // 3.查询已点赞笔记的相关数据以及categoryName：type为3
        List<ArticleTypeDto> articleTypeDtoList3 = articleMapper.getAllByUserIdLikesArticles(userId);
        int listLength3=articleTypeDtoList3.size()+listLength2;

        // 4.遍历每一篇笔记
        articleTypeDtoListAll.addAll(articleTypeDtoList1);
        articleTypeDtoListAll.addAll(articleTypeDtoList2);
        articleTypeDtoListAll.addAll(articleTypeDtoList3);
        int n=0;
        for(ArticleTypeDto articleTypeDto:articleTypeDtoListAll){
            // 4.1查询RelArticleTag中的相关数据
            List<Tag> tmpTList=relArticleTagMapper.getAllByArticleIdTags(articleTypeDto.getArticleId());
            articleTypeDto.setTagList(tmpTList);
            // 4.2统计Likes中的相关数据
            int tmpALNum=articleMapper.getLikesNumByArticleId(articleTypeDto.getArticleId());
            articleTypeDto.setLikesNum(tmpALNum);
            // 4.3统计Collection中的相关数据
            int tmpACNum=articleMapper.getCollectionNumByArticleId(articleTypeDto.getArticleId());
            articleTypeDto.setCollectionNum(tmpACNum);
            // 4.4设置type
            if(n<listLength1)
                articleTypeDto.setType(1);
            else if(n<listLength2)
                articleTypeDto.setType(2);
            else articleTypeDto.setType(3);
            n++;
        }
        // 5.返回结果
        return articleTypeDtoListAll;
    }

    /**
     * 发布笔记
     * @param articleWriteAndUpdateDto 数据传送实体类ArticleWriteDto
     * @return 成功为生成的articleId,失败为null
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer writeArticleService(ArticleWriteAndUpdateDto articleWriteAndUpdateDto) {
        Article article=new Article();
        article.setArticleTitle(articleWriteAndUpdateDto.getArticleTitle());
        article.setArticleCover(articleWriteAndUpdateDto.getArticleCover());
        article.setArticleContent(articleWriteAndUpdateDto.getArticleContent());
        article.setCategoryId(articleWriteAndUpdateDto.getCategoryId());
        article.setCreateTime(articleWriteAndUpdateDto.getCreateTime());
        article.setUpdateTime(articleWriteAndUpdateDto.getCreateTime());
        article.setUserId(articleWriteAndUpdateDto.getUserId());
        if(articleMapper.insert(article) != 1)
            return null;
        else{
            Integer articleId=article.getArticleId();
            for(Tag tag: articleWriteAndUpdateDto.getTagList()){
                RelArticleTag relArticleTag=new RelArticleTag();
                relArticleTag.setArticleId(articleId);
                relArticleTag.setTagId(tag.getTagId());
                if(relArticleTagMapper.insert(relArticleTag) != 1)
                    return null;
            }
            return articleId;
        }
    }

    /**
     * 修改笔记
     * @param articleWriteAndUpdateDto 数据传送实体类ArticleWriteDto
     * @return 成功为true,失败为false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateArticleService(ArticleWriteAndUpdateDto articleWriteAndUpdateDto) {
        Article article=new Article();
        article.setArticleId(articleWriteAndUpdateDto.getArticleId());
        article.setArticleTitle(articleWriteAndUpdateDto.getArticleTitle());
        article.setArticleCover(articleWriteAndUpdateDto.getArticleCover());
        article.setArticleContent(articleWriteAndUpdateDto.getArticleContent());
        article.setCategoryId(articleWriteAndUpdateDto.getCategoryId());
        article.setCreateTime(articleWriteAndUpdateDto.getCreateTime());
        article.setUpdateTime(articleWriteAndUpdateDto.getCreateTime());
        article.setUserId(articleWriteAndUpdateDto.getUserId());
        if(articleMapper.updateById(article) != 1)
            return false;
        else{
            Integer articleId=article.getArticleId();
            // 将原有标签删除
            Map<String,Object> map = new HashMap<>();
            map.put("article_id",articleId);
            relArticleTagMapper.deleteByMap(map);
            // 加入新的标签
            for(Tag tag: articleWriteAndUpdateDto.getTagList()){
                RelArticleTag relArticleTag=new RelArticleTag();
                relArticleTag.setArticleId(articleId);
                relArticleTag.setTagId(tag.getTagId());
                if(relArticleTagMapper.insert(relArticleTag) != 1)
                    return false;
            }
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
     * @param userName 用户名
     * @return 图片转换为base64格式的字符串编码
     */
    @Override
    public String updateUserAvatarService(MultipartFile imageFile, String userName) {
        String imageBase64 = ImageToBase64Utils.ImageToBase64(imageFile);
        if(imageBase64 != null && !imageBase64.equals("0") && !imageBase64.equals("1") && !imageBase64.equals("2")){
            LambdaUpdateWrapper<UserInfo> luw=new LambdaUpdateWrapper<>();
            luw.set(UserInfo::getUserAvatar,imageBase64)
                    .eq(UserInfo::getUsername,userName);
            userMapper.update(null,luw);
        }
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
    public boolean updatePasswordService(UpdatePasswordDto updatePasswordDto) {
       List<UserInfo> u=userMapper.CheckisR(updatePasswordDto.getUserId(),updatePasswordDto.getPassword());
       if(u.size()==0)
           return false;
       userMapper.updatePasswordCon(updatePasswordDto.getUserId(),updatePasswordDto.getNewPassword());
       return true;

    }

    /**
     * 修改用户名
     * @param
     * @param
     * @return
     */
    @Override
    public boolean updateUserNameService(UpdateUserNameDto updateUserNameDto) {
        List<UserInfo> a = userMapper.CheckisR2(updateUserNameDto.getNewUserName());
        if(a.size()!=0)
            return false;
        userMapper.updateUserNameCon(updateUserNameDto.getUserId(),updateUserNameDto.getNewUserName());
        return true;
    }

    /**
     * 获取用户信息
     * @param
     * @param
     * @return
     */
    @Override
    public List<UserInfo> getUserInfoService(String userId) {
        return userMapper.getUserInfoCon(userId);

    }

}
