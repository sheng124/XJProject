package com.xjdzy.controller;

import com.xjdzy.dto.ArticleTypeDto;
import com.xjdzy.dto.FoFoLiCoNumDto;
import com.xjdzy.dto.Result;
import com.xjdzy.entity.Follow;
import com.xjdzy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 关注
     * @param follow 关注实体类
     * @return Result：data为null
     */
    @PostMapping("/user/follow")
    public Result followCon(@RequestBody Follow follow){
        log.info("获取到的数据："+ follow);
        boolean res=userService.followService(follow);
        log.info("Service处理的结果："+ res);
        if(res){
            return Result.success();
        }
        else{
            return Result.error("关注失败!");
        }
    }

    /**
     * 取消关注
     * @param follow 关注实体类
     * @return Result：data为null
     */
    @DeleteMapping("/user/unfollow")
    public Result unFollowCon(@RequestBody Follow follow){
        log.info("获取到的数据："+ follow);
        boolean res=userService.unFollowService(follow);
        log.info("Service处理的结果："+ res);
        if(res){
            return Result.success();
        }
        else{
            return Result.error("关注失败!");
        }
    }


    /**
     * 获取用户关注数、粉丝数、获赞与收藏数
     * @param userId 用户ID
     * @return Result：data为关注数、粉丝数、获赞与收藏数
     */
    @GetMapping("/user/data/{userId}")
    public Result getFoFoLiCoNumCon(@PathVariable Integer userId){
        log.info("获取到的数据："+ userId);
        FoFoLiCoNumDto res=userService.getFoFoLiCoNumService(userId);
        log.info("Service处理的结果："+ res);
        if(res != null){
            return Result.success(res);
        }
        else{
            return Result.error("获取失败!");
        }
    }

    /**
     * 获取已发布、已收藏、已点赞笔记
     * @param userId 用户ID
     * @return Result：data为数据传送对象ArticleTypeDto
     */
    @GetMapping("/user/articles/{userId}")
    public Result getWrLiCoArticlesCon(@PathVariable Integer userId){
        log.info("获取到的数据："+ userId);
        List<ArticleTypeDto> res=userService.getWrLiCoArticlesService(userId);
        log.info("Service处理的结果："+ res);
        if(res != null){
            return Result.success(res);
        }
        else{
            return Result.error("获取失败!");
        }
    }
}
