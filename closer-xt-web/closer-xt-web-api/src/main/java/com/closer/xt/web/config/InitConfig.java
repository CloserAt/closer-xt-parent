package com.closer.xt.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.closer.xt.common.service","com.closer.xt.common.cache"})
public class InitConfig {
}
