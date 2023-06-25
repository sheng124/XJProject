package com.xjdzy.service.Implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xjdzy.mapper.UserMapper;
import com.xjdzy.entity.UserInfo;
import com.xjdzy.service.RedisService;
import com.xjdzy.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterServiceImplements implements RegisterService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public String register(UserInfo userInfo) {
        //构造条件
        LambdaQueryWrapper<UserInfo> lqw=new LambdaQueryWrapper<UserInfo>();
        lqw.eq(UserInfo::getUsername,userInfo.getUsername())
                .eq(UserInfo::getPassword,userInfo.getPassword());
        //查询用户名是否已存在
        List<UserInfo> userInfoList = userMapper.selectList(lqw);
        System.out.println("userInfoList："+userInfoList);
        if(!userInfoList.isEmpty())
            return "USER_REPEAT";
        else {
            if(userMapper.insert(userInfo)==1) {
                redisService.saveUserInfo(String.valueOf(userInfo.getUserId()),userInfo);
                return "REGISTER_SUCCESS";
            }
            else
                return "REGISTER_ERROR";
        }
    }
}
