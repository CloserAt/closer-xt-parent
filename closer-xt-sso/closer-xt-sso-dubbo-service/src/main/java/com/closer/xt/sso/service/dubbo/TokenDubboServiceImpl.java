package com.closer.xt.sso.service.dubbo;

import com.closer.xt.sso.domain.TokenDomain;
import com.closer.xt.sso.domain.repository.TokenDomainRepository;
import com.closer.xt.sso.service.TokenService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

//暴露的服务是 TokenService.checkToken()，服务的版本号是：1.0.0
//interfaceClass = TokenService.class 指明接口是谁，为了让dubbo知道，这样用事务的时候才能对应的去使用，不然加事务注解会出问题
@DubboService(version = "1.0.1", interfaceClass = TokenService.class)//此注解可以将服务注册到nacos注册中心上
public class TokenDubboServiceImpl extends AbstractService implements TokenService {
    @Autowired
    private TokenDomainRepository tokenDomainRepository;

    @Override
    public Long checkToken(String token) {
        TokenDomain tokenDomain = tokenDomainRepository.createDomain();
        return tokenDomain.checkToken(token);
    }
}
