package com.closer.xt.admin.service.impl;

import com.closer.xt.admin.domain.AdminUserDomain;
import com.closer.xt.admin.domain.repository.AdminUserDomainRepository;
import com.closer.xt.admin.model.AdminUserModel;
import com.closer.xt.admin.params.AdminUserParams;
import com.closer.xt.admin.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    private AdminUserDomainRepository adminUserDomainRepository;

    @Override
    public AdminUserModel findUserByUsername(String username) {
        AdminUserParams adminUserParams = new AdminUserParams();
        adminUserParams.setUsername(username);
        AdminUserDomain adminUserDomain = adminUserDomainRepository.createDomain(adminUserParams);
        return adminUserDomain.findUserByUsername();
    }
}
