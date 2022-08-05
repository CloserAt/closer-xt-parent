package com.closer.xt.sso.dubbo.config;

import com.closer.xt.sso.domain.repository.TokenDomainRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"com.closer.xt.common.service"})
@Import(TokenDomainRepository.class)//手动添加需要导入的类
public class InitConfig {
}
