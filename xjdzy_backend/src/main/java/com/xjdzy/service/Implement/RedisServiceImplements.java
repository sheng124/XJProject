package com.xjdzy.service.Implement;

import com.xjdzy.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImplements implements RedisService {

    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;


}
