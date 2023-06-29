package com.xjdzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjdzy.entity.Follow;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowMapper extends BaseMapper<Follow> {
    List<Follow> getByUserIdAndFUserId(Integer userId, Integer fUserId);
}
