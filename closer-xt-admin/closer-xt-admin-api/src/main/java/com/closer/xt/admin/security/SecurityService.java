package com.closer.xt.admin.security;

import com.closer.xt.admin.model.AdminUserModel;
import com.closer.xt.admin.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SecurityService implements UserDetailsService {
    @Autowired
    private AdminUserService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /**
         * username为登陆用户名
         * 根据用户名查到用户数据 一个用户对应一条数据
         * 拿到用户密码，组装Security的User对象，剩下的验证就交给Security
         */
        AdminUserModel admin = adminService.findUserByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        //此处得到的密码是配置类中BCryptPasswordEncoder加密过的
        String password = admin.getPassword();

        List<GrantedAuthority> authorityList = new ArrayList<>();
        User user = new User(username, password, authorityList);
        return user;
    }
}
