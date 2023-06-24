package com.xjdzy.service.Implement;

import com.xjdzy.service.RedisService;
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
     * @param token
     * @return
     */
    @Override
    public boolean deleteToken(String token) {
        stringRedisTemplate.delete(token);
        return true;
    }
}
