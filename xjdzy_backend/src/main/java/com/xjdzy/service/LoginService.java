package com.xjdzy.service;

import com.xjdzy.dto.LoginDto;
import com.xjdzy.entity.UserInfo;

public interface LoginService {

    LoginDto loginJudge(UserInfo userInfo);

    boolean loginOut(String token);
}
