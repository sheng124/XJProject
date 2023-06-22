package com.xjdzy.controller;

import com.xjdzy.dto.*;
import com.xjdzy.entity.Follow;
import com.xjdzy.service.UserService;
import com.xjdzy.utils.JwtAndLoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 发布笔记
     * @param articleWriteAndUpdateDto 数据传送实体类ArticleWriteDto
     * @return Result：data为生成的articleId
     */
    @PostMapping("/user/articles")
    public Result writeArticleCon(@RequestBody ArticleWriteAndUpdateDto articleWriteAndUpdateDto){
        log.info("获取到的数据："+ articleWriteAndUpdateDto);
        Integer res=userService.writeArticleService(articleWriteAndUpdateDto);
        log.info("Service处理的结果："+ res);
        if(res != null){
            return Result.success(res);
        }
        else{
            return Result.error("发布失败!");
        }
    }

    /**
     * 修改笔记
     * @param articleWriteAndUpdateDto 数据传送实体类ArticleWriteDto
     * @return Result：data为null
     */
    @PostMapping("/user/articles/edit")
    public Result updateArticleCon(@RequestBody ArticleWriteAndUpdateDto articleWriteAndUpdateDto){
        log.info("获取到的数据："+ articleWriteAndUpdateDto);
        boolean res=userService.updateArticleService(articleWriteAndUpdateDto);
        log.info("Service处理的结果："+ res);
        if(res){
            return Result.success();
        }
        else{
            return Result.error("修改失败!");
        }
    }

    /**
     * 删除笔记
     * @param articleId 笔记ID
     * @return Result：data为null
     */
    @DeleteMapping("/user/articles/{articleId}")
    public Result deleteArticleCon(@PathVariable Integer articleId){
        log.info("获取到的数据："+ articleId);
        boolean res=userService.deleteArticleService(articleId);
        log.info("Service处理的结果："+ res);
        if(res){
            return Result.success();
        }
        else{
            return Result.error("删除失败!");
        }
    }

    /**
     * 获取用户所有笔记相关数据
     * @param userId 用户ID
     * @return Result：data为数据传送对象ArticlesDataDto
     */
    @GetMapping("/user/articles/data/{userId}")
    public Result getAllArticleDataCon(@PathVariable Integer userId){
        log.info("获取到的数据："+ userId);
        ArticlesDataDto res=userService.getAllArticleDataService(userId);
        log.info("Service处理的结果："+ res);
        if(res != null){
            return Result.success(res);
        }
        else{
            return Result.error("获取失败!");
        }
    }

    /**
     * 上传用户头像
     * @param imageFile 头像文件
     * @return Result：图片转换为base64格式的字符串编码
     */
    @PostMapping("/user/avatar")
    public Result updateUserAvatarCon(MultipartFile imageFile, HttpServletRequest request){
        String userName = (String) JwtAndLoginUtils.parseJWT(request.getHeader("token")).get("user_name");
        log.info("上传用户头像，解析到的userName："+userName);
        String res=userService.updateUserAvatarService(imageFile,userName);
        // 结果过长，不再输出
        // log.info("Service处理的结果："+ res);
        if(res == null){
            return Result.error("上传失败!");
        }
        else if(res.equals("0")){
            return Result.error("图片为空！");
        }
        else if(res.equals("1")){
            return Result.error("图片格式错误！");
        }
        else if(res.equals("2")){
            return Result.error("图片大小超过16MB！");
        }
        else{
            return Result.success(res);
        }
    }
}