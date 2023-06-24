package com.xjdzy.service.Implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xjdzy.anno.Log;
import com.xjdzy.mapper.UserMapper;
import com.xjdzy.entity.UserInfo;
import com.xjdzy.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImplements implements LoginService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Log
    @Override
    public UserInfo loginJudge(UserInfo userInfo) {
        //构造条件
        LambdaQueryWrapper<UserInfo> lqw= new LambdaQueryWrapper<>();
        lqw.eq(UserInfo::getUsername,userInfo.getUsername())
                .eq(UserInfo::getPassword,userInfo.getPassword());
        //查询用户名是否已存在
        return userMapper.selectOne(lqw);
    }
}
