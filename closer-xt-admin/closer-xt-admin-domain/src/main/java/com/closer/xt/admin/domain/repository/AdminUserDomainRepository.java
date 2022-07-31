package com.closer.xt.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.closer.xt.admin.dao.AdminUserMapper;
import com.closer.xt.admin.dao.data.AdminUser;
import com.closer.xt.admin.domain.AdminUserDomain;
import com.closer.xt.admin.params.AdminUserParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminUserDomainRepository {

    @Autowired
    private AdminUserMapper adminUserMapper;

    public AdminUserDomain createDomain(AdminUserParams adminUserParams) {
        return new AdminUserDomain(this, adminUserParams);
    }

    public AdminUser findUserByUsername(String username) {
        LambdaQueryWrapper<AdminUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminUser::getUsername, username);
        queryWrapper.last("limit 1");
        return adminUserMapper.selectOne(queryWrapper);
    }
}
