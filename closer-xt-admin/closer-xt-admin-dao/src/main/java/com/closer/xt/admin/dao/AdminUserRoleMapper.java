package com.closer.xt.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.closer.xt.admin.dao.data.AdminRole;
import com.closer.xt.admin.dao.data.AdminUser;
import com.closer.xt.admin.dao.data.AdminUserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminUserRoleMapper extends BaseMapper<AdminUserRole> {
}
