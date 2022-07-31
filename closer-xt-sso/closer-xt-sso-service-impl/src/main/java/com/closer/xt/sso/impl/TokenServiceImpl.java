package com.closer.xt.sso.impl;

import com.closer.xt.sso.domain.TokenDomain;
import com.closer.xt.sso.domain.repository.TokenDomainRepository;
import com.closer.xt.sso.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl extends AbstractService implements TokenService {

    @Autowired
    private TokenDomainRepository tokenDomainRepository;

    @Override
    public Long checkToken(String token) {
        TokenDomain tokenDomain = tokenDomainRepository.createDomain();
        return tokenDomain.checkToken(token);
    }
}
