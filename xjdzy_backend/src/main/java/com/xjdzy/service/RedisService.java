package com.xjdzy.service;

public interface RedisService {

    boolean checkToken(String token);

    boolean saveToken(String token);

    boolean deleteToken(String token);
}
