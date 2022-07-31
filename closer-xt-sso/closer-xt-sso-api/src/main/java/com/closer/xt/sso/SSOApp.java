package com.closer.xt.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//默认扫包路径是当前包和子包，如果需要其他包的类，则需要自定义扫包路径
@SpringBootApplication
public class SSOApp {
    public static void main(String[] args) {
        SpringApplication.run(SSOApp.class, args);
    }
}
