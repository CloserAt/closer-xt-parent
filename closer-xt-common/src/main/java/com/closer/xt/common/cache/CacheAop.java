package com.closer.xt.common.cache;

import com.alibaba.fastjson.JSON;
import com.closer.xt.common.model.CallResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

@Aspect
@Component
@Slf4j
public class CacheAop {
    @Autowired
    private StringRedisTemplate redisTemplate;

    //定义切点 Cache注解所在的位置，就是切点
    @Pointcut("@annotation(com.closer.xt.common.cache.Cache)")//定义切点方法
    public void pt(){}

    //定义通知
    //代表对切点标识的方法 进行前后的切面处理 方法前后进行增强
    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) {
        try {
            Signature signature = pjp.getSignature();
            //类名
            String className = pjp.getTarget().getClass().getSimpleName();
            //调用的方法名
            String methodName = signature.getName();


            Class[] parameterTypes = new Class[pjp.getArgs().length];
            Object[] args = pjp.getArgs();
            //参数
            String params = "";
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    params += JSON.toJSONString(args[i]);
                    parameterTypes[i] = args[i].getClass();
                } else {
                    parameterTypes[i] = null;
                }
            }
            if (StringUtils.isNotBlank(params)) {
                //加密 以防出现key过长以及字符转义获取不到的情况
                params = DigestUtils.md5Hex(params);
            }
            Method method = pjp.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
            //获取CacheAnnotation注解
            Cache annotation = method.getAnnotation(Cache.class);
            //缓存过期时间
            long time = annotation.time();
            //缓存名称
            String name = annotation.name();
            //先从redis获取
            String redisKey = name + "::" + className + "::" + methodName + "::" + params;//设置redis的key
            String redisValue = redisTemplate.opsForValue().get(redisKey);
            if (StringUtils.isNotBlank(redisValue)) {
                log.info("走了缓存~~~,{},{}", className, methodName);
                return JSON.parseObject(redisValue, CallResult.class);
            }
            Object proceed = pjp.proceed();
            redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(proceed), Duration.ofMillis(time));
            log.info("存入缓存~~~ {},{}", className, methodName);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return CallResult.fail(-999, "系统错误");
    }
}
