package com.closer.xt.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    //声明 密码使用BCrypt来加密的
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/login.html").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/plugins/**").permitAll()
                //权限校验，即符合以下接口路径规则的，进行权限校验
                .antMatchers(
                        "/course/**",
                        "/news/**",
                        "/subject/**",
                        "/topic/**",
                        "/order/**",
                        "/user/menu/userMenuList",
                        "/xt/**").access("@authService.auth(request,authentication)")

                .anyRequest().authenticated()
                .and().headers().frameOptions().disable()
                .and()
                .formLogin()
                .defaultSuccessUrl("/pages/main.ftl")
                .permitAll()
                .and().logout()
                .and().csrf().disable()
        ;
//        super.configure(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}
