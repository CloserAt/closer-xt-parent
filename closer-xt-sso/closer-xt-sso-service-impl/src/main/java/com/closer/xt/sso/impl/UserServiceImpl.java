package com.closer.xt.sso.impl;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.service.AbstractTemplateAction;
import com.closer.xt.sso.domain.UserDomain;
import com.closer.xt.sso.domain.repository.UserDomainRepository;
import com.closer.xt.sso.model.params.UserParams;
import com.closer.xt.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends AbstractService implements UserService {
    @Autowired
    private UserDomainRepository userDomainRepository;

    @Override
    public CallResult userInfo() {
        UserDomain userDomain = userDomainRepository.createDomain(new UserParams());
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return userDomain.userInfo();
            }
        });
    }
}
