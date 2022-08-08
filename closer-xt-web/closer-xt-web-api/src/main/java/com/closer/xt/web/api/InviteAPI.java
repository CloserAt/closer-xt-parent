package com.closer.xt.web.api;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.web.model.params.BillParams;
import com.closer.xt.web.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Controller
@RequestMapping("i")
public class InviteAPI {
    @Autowired
    private BillService billService;

    @PostMapping("all")
    @ResponseBody
    public CallResult all(@RequestBody BillParams billParams) {
        return billService.all(billParams);
    }

    @PostMapping("gen")
    @ResponseBody
    public CallResult gen(@RequestBody BillParams billParams) {
        return billService.gen(billParams);
    }

    @PostMapping("u/{billType}/{id}")
    public String url(HttpServletRequest request,
                          HttpServletResponse response,
                          @PathVariable("billType") String billType,
                          @PathVariable("id") String id) {
        if (billType != null && id != null) {
            //将信息埋入cookie，后续此用户做任何操作，我们都可以判断cookie当中是否有推荐人信息
            Cookie cookie = new Cookie("_i_ga_b"+billType,id);
            //cookie过期时间
            cookie.setMaxAge(3*24*3600);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        //当用户访问推广链接时，需要跳转到应用首页
        String ua = request.getHeader("user-agent").toLowerCase();
        if (ua.indexOf("micromessager") > 0) {
            //微信浏览器 跳转微信登陆
            return "redirect:/api/sso/login/authorize";
        }
        return "redirect:http://www.mszlu.com";
    }
}
