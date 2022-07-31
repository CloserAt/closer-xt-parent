package com.closer.xt.admin.service;

import com.closer.xt.admin.model.AdminUserModel;

public interface AdminUserService {
   AdminUserModel findUserByUsername(String username);
}
