package com.xjdzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjdzy.dto.UpdatePasswordDto;
import com.xjdzy.entity.UserInfo;
import com.xjdzy.service.UserService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {
    //检查
    List<UserInfo> CheckisR(Integer userId , String password);

    Boolean updatePasswordCon( Integer userId , String newPassword);

    List<UserInfo> CheckisR2(String newUserName);

    Boolean updateUserNameCon(Integer userId,String newUserName);

    List<UserInfo> getUserInfoCon(String userId);

    List<UserInfo> getFollowingByUserId(Integer userId);

    List<UserInfo> getFollowerByUserId(Integer userId);
}
