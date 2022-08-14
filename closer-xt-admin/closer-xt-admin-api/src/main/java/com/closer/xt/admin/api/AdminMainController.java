package com.closer.xt.admin.api;

import com.closer.xt.admin.params.AdminUserParams;
import com.closer.xt.admin.service.AdminUserService;
import com.closer.xt.common.model.CallResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
//模板控制类
@Controller
@RequestMapping("xt")
public class AdminMainController {
    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping("index")
    public ModelAndView mainPage() {
        ModelAndView modelAndView = new ModelAndView();
        SecurityContext securityContext = SecurityContextHolder.getContext();//得到springsecurity上下文
        Authentication authentication = securityContext.getAuthentication();//然后获得认证信息
        Object principal = authentication.getPrincipal();//认证信息中的主要信息
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;//强转
            modelAndView.addObject("username",userDetails.getUsername());//放入数据-用户名
        }
        CallResult callResult = this.adminUserService.userMenuList(new AdminUserParams());
        modelAndView.setViewName("main");
        return modelAndView;
    }
}
