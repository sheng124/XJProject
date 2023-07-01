package com.xjdzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjdzy.entity.ChatRecords;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatRecordsMapper extends BaseMapper<ChatRecords> {
    List<ChatRecords> getAllMessageByUserId(Integer userId);

    void setIsReadByUserId(Integer userId);
}
