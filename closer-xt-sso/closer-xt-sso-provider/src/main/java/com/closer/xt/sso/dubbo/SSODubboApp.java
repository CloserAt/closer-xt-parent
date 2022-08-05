package com.closer.xt.sso.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//默认扫包是在当前目录或者子目录下
@SpringBootApplication
public class SSODubboApp {
    public static void main(String[] args) {
        SpringApplication.run(SSODubboApp.class,args);
    }
}
