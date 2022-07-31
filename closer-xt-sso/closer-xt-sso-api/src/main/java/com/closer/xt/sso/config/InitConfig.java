package com.closer.xt.sso.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan({"com.closer.xt.common.wexin.config","com.closer.xt.common.service"})
public class InitConfig {
}