package com.closer.xt.admin.service.impl;

import com.closer.xt.admin.dao.data.AdminPermission;
import com.closer.xt.admin.dao.data.AdminRole;
import com.closer.xt.admin.domain.AdminUserDomain;
import com.closer.xt.admin.domain.repository.AdminUserDomainRepository;
import com.closer.xt.admin.model.AdminUserModel;
import com.closer.xt.admin.params.AdminUserParams;
import com.closer.xt.admin.service.AdminUserService;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.service.AbstractTemplateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminUserServiceImpl extends AbstractService implements AdminUserService {
    @Autowired
    private AdminUserDomainRepository adminUserDomainRepository;

    @Override
    public AdminUserModel findUserByUsername(String username) {
        AdminUserParams adminUserParams = new AdminUserParams();
        adminUserParams.setUsername(username);
        AdminUserDomain adminUserDomain = adminUserDomainRepository.createDomain(adminUserParams);
        return adminUserDomain.findUserByUsername();
    }

    @Override
    public boolean auth(String requestURI, Long userId) {
        AdminUserDomain adminUserDomain = adminUserDomainRepository.createDomain(new AdminUserParams());
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Boolean>() {
            @Override
            public CallResult<Boolean> doAction() {
                return adminUserDomain.auth(requestURI,userId);
            }
        }).getResult();
    }

    @Override
    public CallResult findRolePage(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.findRolePage(adminUserParams);
            }
        });
    }

    @Override
    public CallResult permissionAll(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.permissionAll(adminUserParams);
            }
        });
    }

    @Override
    @Transactional
    public CallResult add(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.add();
            }
        });
    }

    @Override
    public CallResult findRoleById(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.findRoleById(adminUserParams);
            }
        });
    }

    @Override
    @Transactional
    public CallResult updateRole(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.updateRole(adminUserParams);
            }
        });
    }

    @Override
    public CallResult findPermissionPage(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.findPermissionPage(adminUserParams);
            }
        });
    }

    @Override
    @Transactional
    public CallResult addPermission(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            //添加数据检查

            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.addPermission(adminUserParams);
            }
        });
    }

    @Override
    public CallResult findPermissionById(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.findPermissionById(adminUserParams);
            }
        });
    }

    @Override
    @Transactional
    public CallResult updatePermission(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.updatePermission(adminUserParams);
            }
        });
    }

    @Override
    public CallResult findPage(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.findPage(adminUserParams);
            }
        });
    }

    @Override
    public CallResult roleAll() {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(new AdminUserParams());
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.roleAll();
            }
        });
    }

    @Override
    @Transactional
    public CallResult addUser(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            //添加数据检查
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.addUser(adminUserParams);
            }
        });
    }

    @Override
    public CallResult findUserById(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.findUserById(adminUserParams);
            }
        });
    }

    @Override
    @Transactional
    public CallResult updateUser(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.updateUser(adminUserParams);
            }
        });
    }

    @Override
    public CallResult findMenuPage(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.findMenuPage(adminUserParams);
            }
        });
    }

    @Override
    public CallResult menuAll() {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(new AdminUserParams());
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.menuAll();
            }
        });
    }

    @Override
    @Transactional
    public CallResult saveMenu(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.saveMenu(adminUserParams);
            }
        });
    }

    @Override
    public CallResult findMenuById(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.findMenuById(adminUserParams);
            }
        });
    }

    @Override
    @Transactional
    public CallResult updateMenu(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.updateMenu(adminUserParams);
            }
        });
    }

    @Override
    public CallResult userMenuList(AdminUserParams adminUserParams) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.userMenuList(adminUserParams);
            }
        });
    }


}
