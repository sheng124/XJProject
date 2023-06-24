package com.xjdzy.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.xjdzy.dto.Result;
import com.xjdzy.service.RedisService;
import com.xjdzy.utils.JwtAndLoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor{

    @Autowired
    RedisService redisService;

    @Override//方法1：Controller运行前执行，true放行，false拦截
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle...");

        //1.判断请求是否是登录请求，如果是，直接放行
        String url= request.getRequestURI();
        log.info("url：" + url);
        if(url.contains("/login")){
            return true;
        }
        //2.如果不是登录请求，获取令牌，判断令牌是否存在，如果不存在则不放行
        String token=request.getHeader("token");
        if(!StringUtils.hasLength(token)){
            JwtAndLoginUtils.writeResponse(response);
            return false;
        }
        //3.如果存在，判断令牌是否在Redis存储的白名单中，如果在，则放行，否则不放行
        if(redisService.checkToken(token)){
            return true;
        }
        else{
            JwtAndLoginUtils.writeResponse(response);
            return false;
        }
    }

    @Override//方法2：Controller运行后执行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle...");
    }

    @Override//方法3：视图渲染完成后运行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion...");
    }
}
