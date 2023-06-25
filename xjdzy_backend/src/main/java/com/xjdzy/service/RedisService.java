package com.xjdzy.service;

import com.xjdzy.entity.UserInfo;

import java.util.List;

public interface RedisService {

    boolean checkToken(String token);

    boolean saveToken(String token);

    boolean deleteToken(String token);

    boolean checkUserInfoExists(String userId);

    String getUserInfo(String userId);

    boolean saveUserInfo(String userId, Object o);

    boolean deleteUserInfo(Integer userId);
}
