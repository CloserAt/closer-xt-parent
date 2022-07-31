package com.closer.xt.admin.domain;

import com.closer.xt.admin.dao.data.AdminUser;
import com.closer.xt.admin.domain.repository.AdminUserDomainRepository;
import com.closer.xt.admin.model.AdminUserModel;
import com.closer.xt.admin.params.AdminUserParams;
import com.closer.xt.common.model.CallResult;
import org.springframework.beans.BeanUtils;

public class AdminUserDomain {
    private AdminUserDomainRepository adminUserDomainRepository;
    private AdminUserParams adminUserParams;


    public AdminUserDomain(AdminUserDomainRepository adminUserDomainRepository, AdminUserParams adminUserParams) {
        this.adminUserDomainRepository = adminUserDomainRepository;
        this.adminUserParams = adminUserParams;
    }

    public AdminUserModel findUserByUsername() {
        AdminUser adminUser = adminUserDomainRepository.findUserByUsername(this.adminUserParams.getUsername());
        AdminUserModel adminUserModel = new AdminUserModel();
        BeanUtils.copyProperties(adminUser, adminUserModel);
        return adminUserModel;
    }
}
