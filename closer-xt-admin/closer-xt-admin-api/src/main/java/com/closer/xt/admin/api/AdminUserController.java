package com.closer.xt.admin.api;

import com.closer.xt.admin.params.AdminUserParams;
import com.closer.xt.admin.service.AdminUserService;
import com.closer.xt.common.model.CallResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping("role/findRolePage")
    public CallResult findRolePage(@RequestBody AdminUserParams adminUserParams) {
        return adminUserService.findRolePage(adminUserParams);
    }

    @RequestMapping("permission/all")
    public CallResult permissionAll(@RequestBody AdminUserParams adminUserParams) {
        return adminUserService.permissionAll(adminUserParams);
    }

    @RequestMapping("role/add")
    public CallResult roleAdd(@RequestBody AdminUserParams adminUserParams) {
        return adminUserService.add(adminUserParams);
    }

    @RequestMapping("role/findRoleById")
    public CallResult findRoleById(@RequestBody AdminUserParams adminUserParams) {
        return adminUserService.findRoleById(adminUserParams);
    }

    @RequestMapping(value = "role/update")
    public CallResult updateRole(@RequestBody AdminUserParams adminUserParams){
        return adminUserService.updateRole(adminUserParams);
    }

    @RequestMapping("permission/findPermissionPage")
    public CallResult findPermissionPage(@RequestBody AdminUserParams adminUserParams) {
        return adminUserService.findPermissionPage(adminUserParams);
    }

    @RequestMapping("permission/add")
    public CallResult addPermission(@RequestBody AdminUserParams adminUserParams) {
        return adminUserService.addPermission(adminUserParams);
    }

    @RequestMapping("permission/findPermissionById")
    public CallResult findPermissionById(@RequestBody AdminUserParams adminUserParams) {
        return adminUserService.findPermissionById(adminUserParams);
    }

    @RequestMapping("permission/update")
    public CallResult updatePermission(@RequestBody AdminUserParams adminUserParams) {
        return adminUserService.updatePermission(adminUserParams);
    }

    @RequestMapping("findPage")
    public CallResult findPage(@RequestBody AdminUserParams adminUserParams) {
        return adminUserService.findPage(adminUserParams);
    }

    @RequestMapping("role/all")
    public CallResult roleAll() {
        return adminUserService.roleAll();
    }

    @RequestMapping("add")
    public CallResult addUser(@RequestBody AdminUserParams adminUserParams) {
        return adminUserService.addUser(adminUserParams);
    }

    @RequestMapping("findUserById")
    public CallResult findUserById(@RequestBody AdminUserParams adminUserParams) {
        return adminUserService.findUserById(adminUserParams);
    }

    @RequestMapping("update")
    public CallResult updateUser(@RequestBody AdminUserParams adminUserParams) {
        return adminUserService.updateUser(adminUserParams);
    }

    @RequestMapping("menu/findMenuPage")
    public CallResult findMenuPage(@RequestBody AdminUserParams adminUserParams) {
        return adminUserService.findMenuPage(adminUserParams);
    }

    @PostMapping("menu/all")
    public CallResult menuAll() {
        return adminUserService.menuAll();
    }

    @RequestMapping(value = "menu/add")
    public CallResult saveMenu(@RequestBody AdminUserParams adminUserParams){
        return adminUserService.saveMenu(adminUserParams);
    }

    @RequestMapping(value = "menu/findMenuById")
    public CallResult findMenuById(@RequestBody AdminUserParams adminUserParams){
        return adminUserService.findMenuById(adminUserParams);
    }

    @RequestMapping(value = "menu/update")
    public CallResult updateMenu(@RequestBody AdminUserParams adminUserParams){
        return adminUserService.updateMenu(adminUserParams);
    }
    @RequestMapping(value = "menu/userMenuList")
    public CallResult userMenuList(){
        return adminUserService.userMenuList(new AdminUserParams());
    }
}
