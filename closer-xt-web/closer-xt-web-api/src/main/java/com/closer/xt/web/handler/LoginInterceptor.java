package com.closer.xt.web.handler;

import com.alibaba.fastjson.JSON;
import com.closer.xt.common.Login.UserThreadLocal;
import com.closer.xt.common.model.BusinessCodeEnum;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.sso.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
//登录拦截器，所有需要登陆才能访问得资源，未登录都会被拦截
public class LoginInterceptor implements HandlerInterceptor {
    //从cookie中拿到对应得token
    //根据token去做对应得认证，通过即可拿到userID
    //通过ThreadLocal将UserId放入其中，后续得接口都可以通过threadLocal方便得拿到用户id
    //一个请求 就是一个线程，一个请求 经过controller,service,domain,dao代码
    //请求完成之后，ThreadLocal随着线程销毁
    //相比于redis:节省内存； 从redis获取信息，就需要连接网络，开销较大

    //使用dubbo提供的服务
    //服务提供方提供的版本是1.0.0
    @DubboReference(version = "1.0.0")//该注解的作用是通过注册中心，去拿提供方的地址，并进行网络调用（TCP，netty,hession），会将参数进行序列化传输，返回值进行反序列化处理
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("-------------------login interceptor start-----------------------");
        log.info("request uri:{}",request.getRequestURI());
        log.info("Hello Java");
        log.info("request method:{}",request.getMethod());
        log.info("-------------------login interceptor end-----------------------");

        //1.从cookie中拿到对应得token
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            returnJson(response);//为null,说明未登录
            return false;
        }
        String token = null;
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if ("t_token".equals(name)) {
                token = cookie.getValue();
            }
        }
        if (StringUtils.isBlank(token)) {
            returnJson(response);
            return false;
        }

        //2.根据token去做对应得认证，通过即可拿到userID
        Long userId = tokenService.checkToken(token);
        if (userId == null) {
            returnJson(response);
            return false;
        }
        UserThreadLocal.put(userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }

    private void returnJson(HttpServletResponse response){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            CallResult callResult = CallResult.fail(BusinessCodeEnum.NO_LOGIN.getCode(),"您的登录已失效，请重新登录");
            writer.print(JSON.toJSONString(callResult));
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(writer != null){
                writer.close();
            }
        }
    }
}
