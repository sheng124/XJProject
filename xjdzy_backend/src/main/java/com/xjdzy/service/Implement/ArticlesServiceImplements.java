package com.xjdzy.service.Implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xjdzy.dto.ArticleDetailDto;
import com.xjdzy.dto.ArticleSummaryDto;
import com.xjdzy.dto.CAndUDto;
import com.xjdzy.dto.LACStatusDTO;
import com.xjdzy.entity.*;
import com.xjdzy.mapper.*;
import com.xjdzy.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ArticlesServiceImplements implements ArticlesService {

    /**
     * 注入需要的Mapper
     */
    @Autowired(required = false)
    private LikesMapper likesMapper;

    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Autowired(required = false)
    private CollectionMapper collectionMapper;

    @Autowired(required = false)
    private ArticleMapper articleMapper;

    @Autowired(required = false)
    private CategoryMapper categoryMapper;

    @Autowired(required = false)
    private RelArticleTagMapper relArticleTagMapper;

    @Autowired(required = false)
    private TagMapper tagMapper;

    /**
     * 点赞
     * @param likes 点赞实体类
     * @return 点赞成功true,点赞失败false
     */
    @Override
    public boolean likesService(Likes likes) {
        return likesMapper.insert(likes) == 1;
    }

    /**
     * 取消点赞
     * @param likes 点赞实体类
     * @return 取消点赞成功true,取消点赞失败false
     */
    @Override
    public boolean unLikesService(Likes likes) {
        Map<String,Object> map=new HashMap<>();
        map.put("user_id",likes.getUserId());
        map.put("article_id",likes.getArticleId());
        return likesMapper.deleteByMap(map) == 1;
    }

    /**
     * 收藏
     * @param collection 收藏实体类
     * @return 收藏成功true,收藏失败false
     */
    @Override
    public boolean collectionService(Collection collection) {
        return collectionMapper.insert(collection) == 1;
    }

    /**
     * 取消收藏
     * @param collection 收藏实体类
     * @return 取消收藏成功true,取消收藏失败false
     */
    @Override
    public boolean unCollectionService(Collection collection) {
        Map<String,Object> map=new HashMap<>();
        map.put("user_id",collection.getUserId());
        map.put("article_id",collection.getArticleId());
        return collectionMapper.deleteByMap(map) == 1;
    }

    /**
     * 评论
     * @param comment 评论实体类
     * @return 评论成功返回评论ID，失败返回null
     */
    @Override
    public Integer commentService(Comment comment) {
        if(commentMapper.insert(comment) == 1){
            return comment.getCommentId();
        }
        else{
            return null;
        }

    }

    /**
     * 删除评论
     * @param commentId 评论ID
     * @return 删除成功返回true,删除失败返回false
     */
    @Override
    public boolean unCommentService(Integer commentId) {
        Map<String,Object> map=new HashMap<>();
        map.put("comment_id",commentId);
        return commentMapper.deleteByMap(map) == 1;
    }

    /**
     * 根据笔记ID获取笔记概要信息
     * @param articleIdList 笔记ID List
     * @return 笔记列表List<ArticleSummaryDto>
     */
    public List<ArticleSummaryDto> getArticleSummaryByArticleId(List<Integer> articleIdList){
        List<ArticleSummaryDto> articleSummaryDtoList = new ArrayList<>();
        for(Integer articleId:articleIdList){
            // 1.获取基本信息：Article表中的基本信息
            ArticleSummaryDto articleSummaryDto = articleMapper.getAllByArticleIdArticleSummaryDtos(articleId).get(0);
            // 2.获取Category信息
            Category category = articleMapper.getCategoryByArticleId(articleId).get(0);
            articleSummaryDto.setCategory(category);
            // 3.获取UserInfo信息
            UserInfo userInfo = articleMapper.getUserInfoByArticleId(articleId).get(0);
            articleSummaryDto.setUserInfo(userInfo);
            // 4.获取RelArticleTag中的相关数据：标签
            List<Tag> tmpTList=relArticleTagMapper.getAllByArticleIdTags(articleSummaryDto.getArticleId());
            articleSummaryDto.setTagList(tmpTList);
            // 5.统计Likes中的相关数据：点赞数
            int tmpALNum=articleMapper.getLikesNumByArticleId(articleSummaryDto.getArticleId());
            articleSummaryDto.setLikesNum(tmpALNum);
            // 6.统计Collection中的相关数据：收藏数
            int tmpACNum=articleMapper.getCollectionNumByArticleId(articleSummaryDto.getArticleId());
            articleSummaryDto.setCollectionNum(tmpACNum);
            // 7.添加到List中
            articleSummaryDtoList.add(articleSummaryDto);
        }
        // 8.返回结果
        return articleSummaryDtoList;
    }

    /**
     * 获取所有笔记：概要信息
     * @return 所有笔记构成的列表
     */
    @Override
    public List<ArticleSummaryDto> getAllArticles() {
        // 查询所有的笔记ID
        List<Integer> articleIdList = articleMapper.getAllArticleId();
        // 查询对应笔记的信息
        return getArticleSummaryByArticleId(articleIdList);
    }

    /**
     * 获取指定类别笔记：概要信息
     * @param categoryId 类别ID
     * @return 指定类别笔记构成的列表
     */
    @Override
    public List<ArticleSummaryDto> getAllArticlesByCategoryId(Integer categoryId) {
        // 查询属于该分类的笔记的ID
        List<Integer> articleIdList = articleMapper.getArticleIdByCategoryId(categoryId);
        // 查询对应笔记的信息
        return getArticleSummaryByArticleId(articleIdList);
    }

    /**
     * 获取除评论以外的所有笔记详细信息
     * @param articleId 笔记ID
     * @param articleDetailDto 空的数据传送对象ArticleDetailDto
     * @return 含数据的数据传送对象ArticleDetailDto
     */
    @Override
    public ArticleDetailDto getArticlesDataByArticleId(Integer articleId,ArticleDetailDto articleDetailDto) {
        // 1.查询Article中的相关数据
        LambdaQueryWrapper<Article> lqw1=new LambdaQueryWrapper<>();
        lqw1.select(Article::getArticleTitle,
                Article::getArticleCover,
                Article::getArticleContent,
                Article::getCreateTime,
                Article::getUpdateTime,
                Article::getViewsNum,
                Article::getCategoryId,
                Article::getVideoUrl)
                .eq(Article::getArticleId,articleId);
        Article tmpA=articleMapper.selectOne(lqw1);
        articleDetailDto.setArticleId(articleId);
        articleDetailDto.setArticleTitle(tmpA.getArticleTitle());
        articleDetailDto.setArticleCover(tmpA.getArticleCover());
        articleDetailDto.setArticleContent(tmpA.getArticleContent());
        articleDetailDto.setCreateTime(tmpA.getCreateTime());
        articleDetailDto.setUpdateTime(tmpA.getUpdateTime());
        articleDetailDto.setViewsNum(tmpA.getViewsNum());
        articleDetailDto.setVideoUrl(tmpA.getVideoUrl());
        // 2.查询Category中的相关数据
        LambdaQueryWrapper<Category> lqw2=new LambdaQueryWrapper<>();
        lqw2.select(Category::getCategoryName)
                .eq(Category::getCategoryId,tmpA.getCategoryId());
        Category tmpC=categoryMapper.selectOne(lqw2);
        articleDetailDto.setCategory(tmpC);
        // 3.查询RelArticleTag中的相关数据
        List<Tag> tmpTL=relArticleTagMapper.getAllByArticleIdTags(articleId);
        articleDetailDto.setTagList(tmpTL);
        // 4.统计Likes中的相关数据
        LambdaQueryWrapper<Likes> lqw4=new LambdaQueryWrapper<>();
        lqw4.eq(Likes::getArticleId,articleId);
        int tpmLCount= Math.toIntExact(likesMapper.selectCount(lqw4));
        articleDetailDto.setLikesNum(tpmLCount);
        // 5.统计Collection中的相关数据
        LambdaQueryWrapper<Collection> lqw5=new LambdaQueryWrapper<>();
        lqw5.eq(Collection::getArticleId,articleId);
        int tpmColCount= Math.toIntExact(collectionMapper.selectCount(lqw5));
        articleDetailDto.setCollectionNum(tpmColCount);
        // 6.统计Comment中的相关数据
        LambdaQueryWrapper<Comment> lqw6=new LambdaQueryWrapper<>();
        lqw6.eq(Comment::getArticleId,articleId);
        int tpmComCount= Math.toIntExact(commentMapper.selectCount(lqw6));
        articleDetailDto.setCommentNum(tpmComCount);

        return articleDetailDto;
    }

    /**
     * 获取笔记详细信息
     * @param articleId 笔记ID
     * @return 数据传送对象ArticleDetailDto
     */
    @Override
    public ArticleDetailDto getArticlesDetailByArticleId(Integer articleId) {
        // 0.创建空的数据传送对象
        ArticleDetailDto articleDetailDto=new ArticleDetailDto();
        getArticlesDataByArticleId(articleId,articleDetailDto);
        // 7.获取发布者的UserInfo数据
        UserInfo userInfo = articleMapper.getUserInfoByArticleId(articleId).get(0);
        articleDetailDto.setUserInfo(userInfo);
        // 8.统计UserInfo和Comment中的相关数据
        List<CAndUDto> tmpCAU=commentMapper.getAllByArticleIdCAndUDtos(articleId);
        articleDetailDto.setCAndUDtoList(tmpCAU);
        // 9.获取照片信息
        articleDetailDto.setArticleImages(articleMapper.getArticleImageByArticleId(articleId));
        // 10.修改浏览量
        articleMapper.updateViewsNumByArticleId(articleId);
        return articleDetailDto;
    }


    /**
     * 获取所有标签
     * @return List<Tag>
     */
    @Override
    public List<Tag> getAllTagsService() {
        return tagMapper.selectList(null);
    }

    /**
     * 增加标签
     * @param tag 需要增加的标签
     * @return 增加成功true，否则false
     */
    @Override
    public boolean addTagsService(Tag tag) {
        return tagMapper.insert(tag) == 1;
    }


    /**
     * 获取点赞、收藏状态
     * @param userId 用户ID
     * @param articleId 笔记ID
     * @return 数据传输对象GetLACStatusDTO
     */
    @Override
    public LACStatusDTO getLACStatusService(Integer userId, Integer articleId) {
        LACStatusDTO lacStatusDTO = new LACStatusDTO();
        lacStatusDTO.setLike(likesMapper.getByUserIdAndArticleId(userId,articleId).size() != 0);
        lacStatusDTO.setCollection(collectionMapper.getByUserIdAndArticleId(userId,articleId).size() != 0);
        return lacStatusDTO;
    }
}
