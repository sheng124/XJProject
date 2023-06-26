package com.xjdzy.service.Implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xjdzy.anno.Log;
import com.xjdzy.dto.LoginDto;
import com.xjdzy.mapper.UserMapper;
import com.xjdzy.entity.UserInfo;
import com.xjdzy.service.LoginService;
import com.xjdzy.service.RedisService;
import com.xjdzy.utils.JwtAndLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImplements implements LoginService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    RedisService redisService;

    @Override
    public LoginDto loginJudge(UserInfo userInfo) {
        // 构造条件
        LambdaQueryWrapper<UserInfo> lqw= new LambdaQueryWrapper<>();
        lqw.eq(UserInfo::getUsername,userInfo.getUsername())
                .eq(UserInfo::getPassword,userInfo.getPassword());
        // 查询用户名是否已存在
        UserInfo u = userMapper.selectOne(lqw);
        if(u == null)
            return null;
        // 生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",u.getUserId());
        claims.put("user_id",u.getUserId());
        claims.put("user_name",u.getUsername());
        claims.put("password",u.getPassword());
        String token = JwtAndLoginUtils.generateJwt(claims);
        // 存入Redis
        redisService.saveToken(token);
        // 将token以及userId返回
        return new LoginDto(token,u.getUserId());
    }

    @Override
    public boolean loginOut(String token) {
        redisService.deleteToken(token);
        return true;
    }
}
