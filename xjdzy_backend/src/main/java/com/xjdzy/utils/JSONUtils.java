package com.xjdzy.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xjdzy.entity.UserInfo;

public class JSONUtils {

    // JSON序列化工具
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 将对象转换为JSON字符串
     * @param object 对象
     * @return JSON字符串
     */
    public static String objectToJSONString(Object object){
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将JSON字符串转换为对象
     * @param jsonString JSON字符串
     * @return 对象
     */
    public static UserInfo JSONStringToUserInfo(String jsonString){
        try {
            return mapper.readValue(jsonString, UserInfo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
