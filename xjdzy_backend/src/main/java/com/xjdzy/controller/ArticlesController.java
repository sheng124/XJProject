package com.xjdzy.controller;


import com.xjdzy.dto.ArticleDetailDto;
import com.xjdzy.dto.ArticleSummaryDto;
import com.xjdzy.dto.LACStatusDTO;
import com.xjdzy.dto.Result;
import com.xjdzy.entity.*;
import com.xjdzy.service.ArticlesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ArticlesController {

    @Autowired
    private ArticlesService articlesService;

    /**
     * 点赞
     * @param likes 点赞实体类
     * @return Result：data为null
     */
    @PostMapping("/articles/like")
    public Result likesCon(@RequestBody Likes likes){
        log.info("获取到的数据："+ likes);
        boolean res=articlesService.likesService(likes);
        log.info("Service处理的结果："+ res);
        if(res){
            return Result.success();
        }
        else{
            return Result.error("点赞失败!");
        }
    }

    /**
     * 取消点赞
     * @param likes 点赞实体类
     * @return Result：data为null
     */
    @DeleteMapping("/articles/unlike")
    public Result unLikeCon(@RequestBody Likes likes){
        log.info("获取到的数据："+ likes);
        boolean res=articlesService.unLikesService(likes);
        log.info("Service处理的结果："+ res);
        if(res){
            return Result.success();
        }
        else{
            return Result.error("取消点赞失败!");
        }
    }

    /**
     * 收藏
     * @param collection  收藏实体类
     * @return Result：data为null
     */
    @PostMapping("/articles/collection")
    public Result collectionCon(@RequestBody Collection collection){
        log.info("获取到的数据："+ collection);
        boolean res=articlesService.collectionService(collection);
        log.info("Service处理的结果："+ res);
        if(res){
            return Result.success();
        }
        else{
            return Result.error("收藏失败!");
        }
    }

    /**
     * 取消收藏
     * @param collection  收藏实体类
     * @return Result：data为null
     */
    @DeleteMapping("/articles/unCollection")
    public Result unCollectionCon(@RequestBody Collection collection){
        log.info("获取到的数据："+ collection);
        boolean res=articlesService.unCollectionService(collection);
        log.info("Service处理的结果："+ res);
        if(res){
            return Result.success();
        }
        else{
            return Result.error("取消收藏失败!");
        }
    }

    /**
     * 评论
     * @param comment 评论实体类
     * @return Result：若成功，data为commentId
     */
    @PostMapping("/articles/comment")
    public Result commentCon(@RequestBody Comment comment){
        log.info("获取到的数据："+ comment);
        Integer commentId=articlesService.commentService(comment);
        log.info("Service处理的结果："+ commentId);
        if(commentId!=null){
            return Result.success(commentId);
        }
        else{
            return Result.error("评论失败!");
        }
    }

    /**
     * 删除评论
     * @param comment 评论实体类
     * @return Result：data为null
     */
    @DeleteMapping("/articles/unComment")
    public Result unCommentCon(@RequestBody Comment comment){
        log.info("获取到的数据："+ comment);
        boolean res=articlesService.unCommentService(comment.getCommentId());
        log.info("Service处理的结果："+ res);
        if(res){
            return Result.success();
        }
        else{
            return Result.error("取消收藏失败!");
        }
    }

    /**
     * 获取所有笔记
     * @return Result：data为笔记列表
     */
    @GetMapping("/articles")
    public Result getAllArticlesCon(){
        log.info("获取所有文章");
        List<ArticleSummaryDto> res=articlesService.getAllArticles();
        log.info("viewsNum:"+res.get(0).getViewsNum());
        return Result.success(res);
    }

    /**
     * 获取指定类别笔记
     * @param categoryId 类别ID
     * @return Result：data为指定类别笔记的列表
     */
    @GetMapping("/articles/{categoryId}")
    public Result getAllArticlesByCategoryIdCon(@PathVariable Integer categoryId){
        log.info("获取到的数据："+categoryId);
        List<ArticleSummaryDto> res=articlesService.getAllArticlesByCategoryId(categoryId);
        log.info("viewsNum："+ res.get(0).getViewsNum());
        return Result.success(res);
    }

    /**
     * 获取指定类别笔记
     * @param tagId 类别ID
     * @return Result：data为指定标签笔记的列表
     */
    @GetMapping("/articles/tag/{tagId}")
    public Result getAllArticlesByTagIdCon(@PathVariable Integer tagId){
        log.info("获取到的数据："+tagId);
        List<ArticleSummaryDto> res=articlesService.getAllArticlesByTagId(tagId);
        log.info("viewsNum："+ res.get(0).getViewsNum());
        return Result.success(res);
    }

    /**
     * 获取笔记详细信息
     * @param articleId  笔记ID
     * @return Result：data为数据传送对象ArticleDetailDto
     */
    @GetMapping("/articles/info/{articleId}")
    public Result getArticlesDetailByArticleIdCon(@PathVariable Integer articleId){
        log.info("获取到的数据："+articleId);
        ArticleDetailDto res=articlesService.getArticlesDetailByArticleId(articleId);
        log.info("viewsNum："+ res.getViewsNum());
        return Result.success(res);
    }

    /**
     * 获取所有标签
     * @return Result：data为Tag列表
     */
    @GetMapping("/articles/tags")
    public Result getAllTagsCon(){
        List<Tag> res=articlesService.getAllTagsService();
        log.info("Service处理的结果："+ res);
        return Result.success(res);
    }

    /**
     * 增加标签
     * @param tag 需要增加的标签
     * @return Result：data为null
     */
    @PostMapping("/articles/tags")
    public Result addTagsCon(@RequestBody Tag tag){
        log.info("获取到的数据："+ tag);
        boolean res=articlesService.addTagsService(tag);
        log.info("Service处理的结果："+ res);
        if(res){
            return Result.success();
        }
        else{
            return Result.error("增加失败！");
        }
    }

    /**
     * 获取点赞、收藏状态
     * @param userId 用户ID
     * @param articleId 笔记ID
     * @return Result：data为数据传输对象GetLACStatusDTO
     */
    @GetMapping("/articles/getLACStatus/{userId}/{articleId}")
    public Result getLACStatusCon(@PathVariable Integer userId,@PathVariable Integer articleId){
        log.info("获取到的数据："+ userId + " " + articleId);
        LACStatusDTO res = articlesService.getLACStatusService(userId,articleId);
        log.info("Service处理的结果："+ res);
        if(res != null){
            return Result.success(res);
        }
        else{
            return Result.error("获取失败！");
        }
    }

    /**
     * 搜索笔记
     * @param keyword 关键词
     * @return 笔记列表，关键高亮
     */
    @GetMapping("articles/search")
    public Result getArticlesByKeywordCon(String keyword){
        log.info("获取到的数据："+ keyword);
        List<Article> res = articlesService.getArticlesByKeywordService(keyword);
        log.info("Service处理的结果："+ res);
        if(res != null){
            return Result.success(res);
        }
        else{
            return Result.error("获取失败！");
        }
    }
}
