package com.xjdzy.service.Implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xjdzy.dto.ArticleTypeDto;
import com.xjdzy.dto.FoFoLiCoNumDto;
import com.xjdzy.entity.Article;
import com.xjdzy.entity.Follow;
import com.xjdzy.entity.Tag;
import com.xjdzy.mapper.ArticleMapper;
import com.xjdzy.mapper.FollowMapper;
import com.xjdzy.mapper.RelArticleTagMapper;
import com.xjdzy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImplements implements UserService {

    /**
     * 注入需要的Mapper
     */
    @Autowired(required = false)
    FollowMapper followMapper;

    @Autowired(required = false)
    ArticleMapper articleMapper;

    @Autowired(required = false)
    RelArticleTagMapper relArticleTagMapper;

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
}
