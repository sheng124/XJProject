package com.xjdzy.controller;

import com.xjdzy.dto.LoginDto;
import com.xjdzy.dto.Result;
import com.xjdzy.entity.UserInfo;
import com.xjdzy.service.LoginService;
import com.xjdzy.utils.JwtAndLoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Result loginCheck(@RequestBody UserInfo userInfo){
        //调用Service获取查询结果
        log.info("获取到的数据："+ userInfo);
        LoginDto res = loginService.loginJudge(userInfo);
        log.info("Service处理的结果："+ res);
        //登录成功，发放令牌以及UserId
        if(res != null){
            return Result.success(res);
        }
        //登录失败，返回错误信息
        return Result.error("用户名或密码不正确！");
    }

    @DeleteMapping("/logout")
    public Result loginOutCon(HttpServletRequest request){
        // 获取token
        String token = request.getHeader("token");
        log.info("获取到的数据："+ token);
        // 调用Service获取查询结果
        boolean res = loginService.loginOut(token);
        log.info("Service处理的结果："+ res);
        // 返回结果
        if(res){
            return Result.success();
        }
        //登录失败，返回错误信息
        return Result.error("退出失败！");
    }
}
