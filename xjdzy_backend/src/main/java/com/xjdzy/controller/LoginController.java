package com.xjdzy.controller;

import com.xjdzy.dto.LoginDto;
import com.xjdzy.dto.Result;
import com.xjdzy.entity.UserInfo;
import com.xjdzy.service.LoginService;
import com.xjdzy.utils.JwtAndLoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        UserInfo u=loginService.loginJudge(userInfo);
        log.info("Service处理的结果："+ u);
        //登录成功，发放令牌以及UserId
        if(u!=null){
            Map<String, Object> claims = new HashMap<>();
            claims.put("id",u.getUserId());
            claims.put("user_name",u.getUsername());
            claims.put("password",u.getPassword());
            String token = JwtAndLoginUtils.generateJwt(claims);
            log.info("令牌："+ token);
            LoginDto loginDto = new LoginDto(token,u.getUserId());
            return Result.success(loginDto);
        }
        //登录失败，返回错误信息
        return Result.error("用户名或密码不正确！");
    }
}
