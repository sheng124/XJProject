package com.xjdzy.service.Implement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xjdzy.entity.UserInfo;
import com.xjdzy.service.RedisService;
import com.xjdzy.utils.JSONUtils;
import com.xjdzy.utils.JwtAndLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImplements implements RedisService {

    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 检查token是否在Redis中
     * @param token 令牌
     * @return 存在返回true,否则返回false
     */
    @Override
    public boolean checkToken(String token) {
        // stringRedisTemplate.hasKey(token)可能为空
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(token));
    }

    /**
     * 将token存入Redis
     * @param token 令牌
     * @return 存入成功返回true,否则返回false
     */
    @Override
    public boolean saveToken(String token) {
        stringRedisTemplate.opsForValue().set(token, String.valueOf(System.currentTimeMillis()),
                JwtAndLoginUtils.expire, TimeUnit.MILLISECONDS);
        return true;
    }

    /**
     * 删除Redis中的token
     * @param token 令牌
     * @return 删除成功true,否则false
     */
    @Override
    public boolean deleteToken(String token) {
        stringRedisTemplate.delete(token);
        return true;
    }

    /**
     * 查看用户信息是否存在
     * @param userId 用户ID
     * @return 存在true,否则false
     */
    @Override
    public boolean checkUserInfoExists(String userId) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(userId));
    }

    /**
     * 返回用户信息
     * @param userId 用户ID
     * @return 查询到的内容
     */
    @Override
    public String getUserInfo(String userId) {
        String value = stringRedisTemplate.opsForValue().get(userId);
        if(value == null)
            return null;
        return value;
    }

    /**
     * 存储用户信息
     * @param userId 用户ID
     * @param o 用户信息
     * @return 成功true，否则false
     */
    @Override
    public boolean saveUserInfo(String userId, Object o) {
        if(o == null){
            stringRedisTemplate.opsForValue().set(userId,"",
                    JwtAndLoginUtils.expire, TimeUnit.MILLISECONDS);
        }
        else{
            String JSONString = JSONUtils.objectToJSONString(o);
            stringRedisTemplate.opsForValue().set(userId, JSONString,
                    JwtAndLoginUtils.expire, TimeUnit.MILLISECONDS);
        }
        return true;
    }

    /**
     * 删除用户信息
     * @param userId 用户ID
     * @return 成功true,否则false
     */
    @Override
    public boolean deleteUserInfo(Integer userId) {
        stringRedisTemplate.delete(String.valueOf(userId));
        return true;
    }
}
