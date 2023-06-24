package com.xjdzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjdzy.entity.RelArticleTag;
import com.xjdzy.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface RelArticleTagMapper extends BaseMapper<RelArticleTag> {

    List<Tag> getAllByArticleIdTags(Integer articleId);
}
