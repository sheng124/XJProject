package com.xjdzy.aop;

import com.alibaba.fastjson.JSONObject;
import com.xjdzy.mapper.OperateLogMapper;
import com.xjdzy.entity.OperateLogInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
@Aspect      //切面类
public class LogAspect {

    @Autowired(required = false)
    private HttpServletRequest request;

    @Autowired(required = false)
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.xjdzy.anno.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //操作人员ID - 当前登录人员ID
        //获取请求头中的jwt令牌, 解析令牌
//        String jwt = request.getHeader("token");
//        Claims claims = JwtAndLoginUtils.parseJWT(jwt);
//        Integer operateUser = (Integer) claims.get("id");

        int i=1;
        Integer operateUser=(Integer) i;

        //操作时间
        LocalDateTime operateTime = LocalDateTime.now();

        //操作类名
        String className = joinPoint.getTarget().getClass().getName();

        //操作方法名
        String methodName = joinPoint.getSignature().getName();

        //操作方法参数
        Object[] args = joinPoint.getArgs();
        String methodParams = Arrays.toString(args);

        long begin = System.currentTimeMillis();
        //调用原始目标方法运行
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();

        //方法返回值
        String returnValue = JSONObject.toJSONString(result);

        //操作耗时
        Long costTime = end - begin;


        //记录操作日志
        OperateLogInfo operateLog = new OperateLogInfo();
        operateLog.setUserId(operateUser);
        operateLog.setOperateTime(operateTime);
        operateLog.setClassName(className);
        operateLog.setMethodName(methodName);
        operateLog.setMethodParams(methodParams);
        operateLog.setReturnValue(returnValue);
        operateLog.setCostTime(costTime);
        operateLogMapper.insert(operateLog);

        log.info("记录操作日志: {}" , operateLog);

        return result;
    }
}
