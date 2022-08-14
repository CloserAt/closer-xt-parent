package com.closer.xt.admin.security;

import com.closer.xt.admin.dao.data.AdminPermission;
import com.closer.xt.admin.dao.data.AdminRole;
import com.closer.xt.admin.model.AdminUserModel;
import com.closer.xt.admin.service.AdminUserService;
import com.closer.xt.common.Login.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AuthService {
    @Autowired
    private AdminUserService adminUserService;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    /**
     *
     * @param request
     * @param authentication
     * @return true代表权限通过 false代表权限不通过
     */
    public boolean auth(HttpServletRequest request, Authentication authentication) {
        /**
         * 1.判断当前的用户是否登陆，如果没有登陆或者登陆失效 就返回false
         * 2.拿到登陆用户的信息，根据用户信息，去查询用户，根据用户id查询角色
         * 3.根据角色去查询对应的角色
         */
        Object principal = authentication.getPrincipal();
        if ("anonymousUser".equals(principal)) {
            //匿名用户 当前未登录
            return false;
        }

        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        AdminUserModel adminUserModel = this.adminUserService.findUserByUsername(username);
        //请求链接：如/lzadmin/course/findPage
        String requestURI = request.getRequestURI();
        requestURI.replace(contextPath,"");
        Long userId = adminUserModel.getId();
        UserThreadLocal.put(userId);
        if ("/user/menu/userMenuList".equals(requestURI) || requestURI.startsWith("/xt")){
            return true;
        }
        return adminUserService.auth(requestURI,userId);
    }
}
