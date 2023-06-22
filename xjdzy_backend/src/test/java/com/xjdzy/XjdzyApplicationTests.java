package com.xjdzy;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xjdzy.entity.RelArticleTag;
import com.xjdzy.mapper.RelArticleTagMapper;
import com.xjdzy.mapper.TesrMapper;

import com.xjdzy.entity.Tesr;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}