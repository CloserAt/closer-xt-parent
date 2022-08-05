package com.closer.xt.admin.config;

import com.closer.xt.common.cache.EnableCache;
import com.closer.xt.common.service.EnableService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan({"com.closer.xt.common.service"})
@EnableCache
@EnableService
public class InitConfig {
}
