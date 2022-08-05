package com.closer.xt.web.config;

import com.closer.xt.common.cache.EnableCache;
import com.closer.xt.common.service.EnableService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan({"com.closer.xt.common.service","com.closer.xt.common.cache"})
@EnableCache
@EnableService
public class InitConfig {
}
