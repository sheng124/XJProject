package com.xjdzy;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xjdzy.entity.RelArticleTag;
import com.xjdzy.entity.UserInfo;
import com.xjdzy.mapper.RelArticleTagMapper;
import com.xjdzy.mapper.TesrMapper;

import com.xjdzy.entity.Tesr;
import net.minidev.json.JSONUtil;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@MapperScan("com/xjdzy/mapper")
class XjdzyApplicationTests {

    @Autowired(required = false)
    private TesrMapper tesrMapper;

    @Autowired(required = false)
    RelArticleTagMapper relArticleTagMapper;

    @Test
    void insertTest() {
        Tesr tesr=new Tesr();
        tesr.setAinfo(4);
        tesr.setBinfo(4);
        tesrMapper.insert(tesr);
        System.out.println(tesr.getId());
    }

    @Test
    void deleteTest(){
        Map<String,Object> map=new HashMap<>();
        map.put("Ainfo",0);
        map.put("Binfo",0);
        tesrMapper.deleteByMap(map);
    }

    @Test
    void ListSize(){
        List<String> l=new ArrayList<>();
        l.add("1");
        l.add("2");
        System.out.println(l.size());
    }

    @Test
    void TestSelect(){
        LambdaQueryWrapper<RelArticleTag> lqw=new LambdaQueryWrapper<>();
        lqw.select(RelArticleTag::getTagId).eq(RelArticleTag::getArticleId,4);
        List<Object> relArticleTags = relArticleTagMapper.selectObjs(lqw);
        System.out.println(relArticleTags);
    }

    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;

    // JSON序列化工具
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void TestRedisCon() throws JsonProcessingException {
        // 设置
        UserInfo u = new UserInfo(3,"03","03",null);
        String uJson = mapper.writeValueAsString(u);
        //stringRedisTemplate.opsForValue().set("user03", uJson);
        // 获取
        //String value = stringRedisTemplate.opsForValue().get("user03");
        UserInfo uB = mapper.readValue(uJson, UserInfo.class);
        System.out.println("uJson = " + uJson);
        System.out.println("uB = " + uB);

    }

    @Test
    void TestTime(){
        System.out.println(System.currentTimeMillis());
    }

    @Test
    void TestVoid(){
        String value = stringRedisTemplate.opsForValue().get("xxx");
        System.out.println(value.equals(""));
    }
}
