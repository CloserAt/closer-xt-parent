package com.closer.xt.sso.api;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.sso.model.params.LoginParams;
import com.closer.xt.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

///api/sso/login/getQRCodeUrl
@Controller
@RequestMapping("login")
public class LoginApi {

    @Autowired
    private LoginService loginService;

    @PostMapping("getQRCodeUrl")
    @ResponseBody
    public CallResult getQRCodeUrl() {
        return loginService.getQRCodeUrl();
    }

    @GetMapping("wxLoginCallBack")
    public String wxLoginCallBack(HttpServletRequest request,
                                  HttpServletResponse response,
                                  String code,
                                  String state) {
        //为了service层统一，所有的api层的参数处理 ，都放入loginParams中
        LoginParams loginParams = new LoginParams();
        loginParams.setCode(code);
        loginParams.setState(state);
        loginParams.setRequest(request);
        //后续 登录成功之后，要生成token，提供给前端，把token放入对应的cookie
        // response.addCookie();
        loginParams.setResponse(response);
        CallResult callResult = loginService.wxLoginCallBack(loginParams);
        if (callResult.isSuccess()) {
            //登陆成功，重定向到course页面
            return "redirect:http://www.mszlu.com/course";
        }
        //登陆失败，重定向到首页
        return "redirect:http://www.mszlu.com";
    }

    //此处测试需要网络，回调得时候，微信方 是通过外网进行得访问，需要内网穿透
    //将一个本地端口，通过内网穿透工具，暴露到外网连接
//    @RequestMapping("authorize")
//    public String authorize() {
//        String redirectUrl = this.loginService.authorize();
//        return "redirect:" + redirectUrl;
//    }
}
