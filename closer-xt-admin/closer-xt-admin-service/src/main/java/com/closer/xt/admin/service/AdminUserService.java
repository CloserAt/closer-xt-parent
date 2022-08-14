package com.closer.xt.admin.service;


import com.closer.xt.admin.model.AdminUserModel;
import com.closer.xt.admin.params.AdminUserParams;
import com.closer.xt.common.model.CallResult;

import java.util.List;

public interface AdminUserService {
   AdminUserModel findUserByUsername(String username);

   boolean auth(String requestURI, Long userId);

    /**
     * 跳转角色页
     * @param adminUserParams
     * @return
     */
    CallResult findRolePage(AdminUserParams adminUserParams);

    /**
     * 查询所有权限
     * @param adminUserParams
     * @return
     */
    CallResult permissionAll(AdminUserParams adminUserParams);

    /**
     * 角色添加
     * @param adminUserParams
     * @return
     */
    CallResult add(AdminUserParams adminUserParams);

    /**
     * 数据回显
     * @param adminUserParams
     * @return
     */
    CallResult findRoleById(AdminUserParams adminUserParams);

    /**
     * 角色更新
     * @param adminUserParams
     * @return
     */
    CallResult updateRole(AdminUserParams adminUserParams);

    /**
     * 权限页分页查询
     * @param adminUserParams
     * @return
     */
   CallResult findPermissionPage(AdminUserParams adminUserParams);

    /**
     * 添加权限
     * @param adminUserParams
     * @return
     */
    CallResult addPermission(AdminUserParams adminUserParams);

     /**
      * 数据回显
      * @param adminUserParams
      * @return
      */
     CallResult findPermissionById(AdminUserParams adminUserParams);

     /**
      * 更新权限
      * @param adminUserParams
      * @return
      */
     CallResult updatePermission(AdminUserParams adminUserParams);

    /**
     * 菜单分页查询
     * @param adminUserParams
     * @return
     */
    CallResult findPage(AdminUserParams adminUserParams);

    /**
     * 查询所有角色
     * @return
     */
    CallResult roleAll();

    /**
     * 新增用户
     * @param adminUserParams
     * @return
     */
    CallResult addUser(AdminUserParams adminUserParams);

    /**
     * 编辑用户-回显
     * @param adminUserParams
     * @return
     */
    CallResult findUserById(AdminUserParams adminUserParams);

   /**
    * 更新用户
    * @param adminUserParams
    * @return
    */
   CallResult updateUser(AdminUserParams adminUserParams);

    /**
     * 菜单分页查询
     * @param adminUserParams
     * @return
     */
    CallResult findMenuPage(AdminUserParams adminUserParams);

    /**
     * 所有菜单
     * @return
     */
    CallResult menuAll();

    /**
     * 保存菜单
     * @param adminUserParams
     * @return
     */
    CallResult saveMenu(AdminUserParams adminUserParams);

    /**
     * 回显-菜单信息
     * @param adminUserParams
     * @return
     */
    CallResult findMenuById(AdminUserParams adminUserParams);

    /**
     * 更新菜单
     * @param adminUserParams
     * @return
     */
    CallResult updateMenu(AdminUserParams adminUserParams);

    /**
     * 所有菜单
     * @param adminUserParams
     * @return
     */
    CallResult userMenuList(AdminUserParams adminUserParams);
}
