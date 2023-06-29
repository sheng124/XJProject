package com.xjdzy.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjdzy.entity.Collection;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {
    List<Collection> getByUserIdAndArticleId(Integer userId, Integer articleId);
}
