package com.closer.xt.sso.domain.repository;

import com.closer.xt.sso.domain.TokenDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TokenDomainRepository {
    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    public TokenDomain createDomain() {
        return new TokenDomain(this);
    }
}
