package com.closer.xt.admin.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.admin.dao.data.AdminMenu;
import com.closer.xt.admin.dao.data.AdminPermission;
import com.closer.xt.admin.dao.data.AdminRole;
import com.closer.xt.admin.dao.data.AdminUser;
import com.closer.xt.admin.domain.repository.AdminUserDomainRepository;
import com.closer.xt.admin.model.AdminMenuModel;
import com.closer.xt.admin.model.AdminUserModel;
import com.closer.xt.admin.params.AdminUserParams;
import com.closer.xt.common.Login.UserThreadLocal;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.model.ListModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.AntPathMatcher;

import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public CallResult<Boolean> auth(String requestURI, Long userId) {
        //有用户和角色关联表 可以直接通过关联表去查询角色id
        List<Integer> roleIdList = this.adminUserDomainRepository.findRoleIdByUserId(userId);
        if (roleIdList == null) {
            return CallResult.fail(false);
        }

        //有角色和权限的关联表，所以可以查询到权限id列表
        List<Integer> permissionIdList = this.adminUserDomainRepository.findPermissionIdListByRoleId(roleIdList);
        if (permissionIdList.isEmpty()){
            return CallResult.fail(false);
        }

        List<AdminPermission> permissionList = this.adminUserDomainRepository.findPermissionByIds(permissionIdList);


        for (AdminPermission permission : permissionList) {
            // /course/**
            String permissionPath = permission.getPermissionPath();
            if (new AntPathMatcher().match(permissionPath,requestURI)){
                return CallResult.success(true);
            }
        }
        return CallResult.fail(false);
    }

    public CallResult<Object> findRolePage(AdminUserParams adminUserParams) {
        int page = adminUserParams.getPage();
        int pageSize = adminUserParams.getPageSize();
        Page<AdminRole> adminRolePage = this.adminUserDomainRepository.findRoleList(page,pageSize);
        ListModel listModel = new ListModel();
        listModel.setTotal((int) adminRolePage.getTotal());
        List<AdminRole> list = adminRolePage.getRecords();
        listModel.setList(list);
        return CallResult.success(listModel);
    }

    public CallResult<Object> permissionAll(AdminUserParams adminUserParams) {
        List<AdminPermission> permissionList = this.adminUserDomainRepository.findAllPermission(adminUserParams);
        return CallResult.success(permissionList);

    }

    public CallResult<Object> add() {
        AdminRole adminRole = new AdminRole();
        BeanUtils.copyProperties(this.adminUserParams,adminRole);
        List<Integer> permissionIdList = this.adminUserParams.getPermissionIdList();
        this.adminUserDomainRepository.saveRole(adminRole);
        Integer roleId = adminRole.getId();
        this.adminUserDomainRepository.saveRolePermission(roleId,permissionIdList);
        return CallResult.success();
    }

    public CallResult<Object> findRoleById(AdminUserParams adminUserParams) {
        Integer roleId = adminUserParams.getRoleId();
        //查询角色
        AdminRole adminRole = this.adminUserDomainRepository.findRoleByRoleId(roleId);

        //根据id查询选中的权限id列表
        List<Integer> permissionIdList = this.adminUserDomainRepository.findPermissionIdListById(roleId);
        Map<String,Object> map = new HashMap<>();
        map.put("role",adminRole);
        map.put("permissionIdList",permissionIdList);
        return CallResult.success(map);

    }

    public CallResult<Object> updateRole(AdminUserParams adminUserParams) {
        AdminRole adminRole = new AdminRole();
        BeanUtils.copyProperties(this.adminUserParams,adminRole);
        adminRole.setId(adminUserParams.getRoleId());
        List<Integer> permissionIdList = this.adminUserParams.getPermissionIdList();
        this.adminUserDomainRepository.updateRole(adminRole);
        Integer roleId = adminRole.getId();
        //先删除关联关系
        this.adminUserDomainRepository.deleteRolePermissionByRoleId(roleId);
        this.adminUserDomainRepository.saveRolePermission(roleId,permissionIdList);
        return CallResult.success();
    }

    public CallResult<Object> findPermissionPage(AdminUserParams adminUserParams) {
        int page = adminUserParams.getPage();
        int pageSize = adminUserParams.getPageSize();
        Page<AdminPermission> permissionPage = this.adminUserDomainRepository.findPermissionPage(page,pageSize);
        ListModel listModel = new ListModel();
        listModel.setTotal((int) permissionPage.getTotal());
        List<AdminPermission> list = permissionPage.getRecords();
        listModel.setList(list);
        return CallResult.success(listModel);
    }

    public CallResult<Object> addPermission(AdminUserParams adminUserParams) {
        AdminPermission adminPermission = new AdminPermission();
        BeanUtils.copyProperties(adminUserParams,adminPermission);
        this.adminUserDomainRepository.savePermission(adminPermission);
        return CallResult.success();
    }

    public CallResult<Object> findPermissionById(AdminUserParams adminUserParams) {
        Integer permissionId = adminUserParams.getPermissionId();
        AdminPermission adminPermission = this.adminUserDomainRepository.findPermissionIdById(permissionId);
        return CallResult.success(adminPermission);
    }

    public CallResult<Object> updatePermission(AdminUserParams adminUserParams) {
        AdminPermission adminPermission = new AdminPermission();
        BeanUtils.copyProperties(adminUserParams,adminPermission);
        adminPermission.setId(adminUserParams.getPermissionId());
        this.adminUserDomainRepository.updatePermission(adminPermission);
        return CallResult.success();
    }

    public CallResult<Object> findPage(AdminUserParams adminUserParams) {
        int page = adminUserParams.getPage();
        int pageSize = adminUserParams.getPageSize();
        Page<AdminUser> adminUserPage = this.adminUserDomainRepository.findUserList(page,pageSize);
        ListModel listModel = new ListModel();
        listModel.setTotal((int) adminUserPage.getTotal());
        List<AdminUser> list = adminUserPage.getRecords();
        listModel.setList(list);
        return CallResult.success(listModel);
    }

    public CallResult<Object> roleAll() {
        List<AdminRole> adminRoleList = this.adminUserDomainRepository.findRoleAll();
        return CallResult.success(adminRoleList);
    }

    public CallResult<Object> addUser(AdminUserParams adminUserParams) {
        /**
         * 1. 密码需要加密
         * 2. 角色存入关联表
         */
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(adminUserParams.getUsername());
        String encodePassword = new BCryptPasswordEncoder().encode(adminUserParams.getPassword());
        adminUser.setPassword(encodePassword);
        this.adminUserDomainRepository.saveUser(adminUser);

        //存入关联表
        List<Integer> roleIdList = adminUserParams.getRoleIdList();
        for (Integer roleId : roleIdList) {
            this.adminUserDomainRepository.saveUserRole(adminUser.getId(),roleId);
        }

        return CallResult.success();
    }

    public CallResult<Object> findUserById(AdminUserParams adminUserParams) {
        Long id = adminUserParams.getId();
        AdminUser adminUser = this.adminUserDomainRepository.findUserById(id);
        List<Integer> adminRoleIdList = this.adminUserDomainRepository.findAdminRoleIdListByUserId(id);
        Map<String,Object> map = new HashMap<>();
        map.put("user",adminUser);
        map.put("roleIdList",adminRoleIdList);
        return CallResult.success(map);
    }

    public CallResult<Object> updateUser(AdminUserParams adminUserParams) {
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(adminUserParams.getUsername());
        String newPassword = adminUserParams.getNewPassword();
        if (StringUtils.isNotBlank(newPassword)) {
            adminUser.setPassword(new BCryptPasswordEncoder().encode(this.adminUserParams.getNewPassword()));
        }
        adminUser.setId(adminUserParams.getId());

        //先删除关联关系，再添加新的
        this.adminUserDomainRepository.updateUser(adminUser);
        this.adminUserDomainRepository.deleteUserRoleByUserId(adminUser.getId());
        List<Integer> roleIdList = adminUserParams.getRoleIdList();
        for (Integer roleId : roleIdList) {
            this.adminUserDomainRepository.saveUserRole(adminUser.getId(),roleId);
        }
        return CallResult.success();
    }

    public CallResult<Object> findMenuPage(AdminUserParams adminUserParams) {
        int page = adminUserParams.getPage();
        int pageSize = adminUserParams.getPageSize();
        Page<AdminMenu> adminMenuPage = this.adminUserDomainRepository.findMenuPage(page,pageSize);
        ListModel listModel = new ListModel();
        listModel.setTotal((int) adminMenuPage.getTotal());
        List<AdminMenu> list = adminMenuPage.getRecords();
        listModel.setList(list);
        return CallResult.success(listModel);
    }

    public CallResult<Object> menuAll() {
        List<AdminMenu> menuList = this.adminUserDomainRepository.findMenuAll();
        AdminMenu adminMenu = new AdminMenu();
        adminMenu.setId(0);
        adminMenu.setLevel(0);
        adminMenu.setMenuName("父菜单");
        menuList.add(adminMenu);
        return CallResult.success(menuList);
    }

    public CallResult<Object> saveMenu(AdminUserParams adminUserParams) {
        AdminMenu adminMenu = new AdminMenu();
        BeanUtils.copyProperties(adminUserParams,adminMenu);
        this.adminUserDomainRepository.saveMenu(adminMenu);
        return CallResult.success();
    }

    public CallResult<Object> findMenuById(AdminUserParams adminUserParams) {
        AdminMenu adminMenu = this.adminUserDomainRepository.findMenuById(adminUserParams.getId());
        return CallResult.success(adminMenu);
    }

    public CallResult<Object> updateMenu(AdminUserParams adminUserParams) {
        AdminMenu adminMenu = new AdminMenu();
        BeanUtils.copyProperties(adminUserParams,adminMenu);
        adminMenu.setId(adminUserParams.getMenuId());
        this.adminUserDomainRepository.updateMenu(adminMenu);
        return CallResult.success();
    }

    public CallResult<Object> userMenuList(AdminUserParams adminUserParams) {
        //此处需要list数据
        List<AdminMenuModel> adminMenuModelList = new ArrayList<>();

        //格局用户进行查询角色，然后根据角色查询菜单
        Long userId = UserThreadLocal.get();
        List<Integer> roleIdList = this.adminUserDomainRepository.findAdminRoleIdListByUserId(userId);
        //根据roleid查询所有的菜单列表
        List<AdminMenu> adminMenuList = this.adminUserDomainRepository.findMenuListByRoleIds(roleIdList);

        //构建树形菜单
        //首先构建第一级菜单
        for (AdminMenu adminMenu : adminMenuList) {
            if (adminMenu.getLevel() == 1) {
                AdminMenuModel adminMenuModel = new AdminMenuModel();
                adminMenuModel.setId(adminMenu.getId());
                adminMenuModel.setTitle(adminMenu.getMenuName());
                adminMenuModel.setIcon("fa-user-md");
                adminMenuModel.setChildren(children(adminMenuModel,adminMenuList));
                adminMenuModel.setLevel(adminMenu.getLevel());
                adminMenuModelList.add(adminMenuModel);
            }
        }

        return CallResult.success(adminMenuModelList);
    }

    //递归形成树形二级菜单
    private List<AdminMenuModel> children(AdminMenuModel adminMenuModel, List<AdminMenu> adminMenuList) {
        List<AdminMenuModel> adminMenuModelList = new ArrayList<>();
        if (adminMenuModel.getLevel() == 2) {
            return adminMenuModelList;
        }
        for (AdminMenu menu : adminMenuList) {
            if (menu.getParentId().equals(adminMenuModel.getId()) && menu.getLevel() != 1) {
                AdminMenuModel amm = new AdminMenuModel();
                amm.setId(menu.getId());
                amm.setTitle(menu.getMenuName());
                amm.setIcon("fa-user-md");
                amm.setLevel(menu.getLevel());
                amm.setLinkUrl(menu.getMenuLink());
                amm.setChildren(children(adminMenuModel,adminMenuList));
                adminMenuModelList.add(amm);
            }
        }
        return adminMenuModelList;
    }
}
