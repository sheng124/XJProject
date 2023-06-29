package com.xjdzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjdzy.entity.Likes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LikesMapper extends BaseMapper<Likes> {
    List<Likes> getByUserIdAndArticleId(Integer userId, Integer articleId);
}
