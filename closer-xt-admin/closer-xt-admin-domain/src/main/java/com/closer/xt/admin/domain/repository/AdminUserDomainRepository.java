package com.closer.xt.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.admin.dao.*;
import com.closer.xt.admin.dao.data.*;
import com.closer.xt.admin.domain.AdminUserDomain;
import com.closer.xt.admin.params.AdminUserParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.security.Permission;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminUserDomainRepository {
    @Resource
    private AdminMenuMapper adminMenuMapper;

    @Resource
    private AdminRoleMenuMapper adminRoleMenuMapper;

    @Resource
    private AdminRoleMapper adminRoleMapper;

    @Resource
    private AdminUserMapper adminUserMapper;

    @Autowired
    private AdminUserRoleMapper adminUserRoleMapper;

    @Autowired
    private AdminPermissionMapper adminPermissionMapper;

    @Autowired
    private AdminRolePermissionMapper adminRolePermissionMapper;

    public AdminUserDomain createDomain(AdminUserParams adminUserParams) {
        return new AdminUserDomain(this, adminUserParams);
    }

    public AdminUser findUserByUsername(String username) {
        LambdaQueryWrapper<AdminUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminUser::getUsername, username);
        queryWrapper.last("limit 1");
        return adminUserMapper.selectOne(queryWrapper);
    }

    public List<Integer> findRoleIdByUserId(Long userId) {
        LambdaQueryWrapper<AdminUserRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AdminUserRole::getUserId,userId);
        List<AdminUserRole> adminUserRoleList = adminUserRoleMapper.selectList(queryWrapper);
        return adminUserRoleList.stream().map(AdminUserRole::getRoleId).collect(Collectors.toList());
    }

    public List<Integer> findPermissionIdListByRoleId(List<Integer> roleIdList) {
        LambdaQueryWrapper<AdminRolePermission> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(AdminRolePermission::getRoleId,roleIdList);
        List<AdminRolePermission> adminRolePermissionList = this.adminRolePermissionMapper.selectList(queryWrapper);
        return adminRolePermissionList.stream().map(AdminRolePermission::getPermissionId).collect(Collectors.toList());
    }
    public List<Integer> findPermissionIdListById(Integer roleId) {
        LambdaQueryWrapper<AdminRolePermission> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AdminRolePermission::getRoleId,roleId);
        return adminRolePermissionMapper.selectList(queryWrapper).stream().map(AdminRolePermission::getPermissionId).collect(Collectors.toList());
    }

    public List<AdminPermission> findPermissionByIds(List<Integer> permissionIdList) {
        LambdaQueryWrapper<AdminPermission> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(AdminPermission::getId,permissionIdList);
        return adminPermissionMapper.selectList(queryWrapper);
    }

    public Page<AdminRole> findRoleList(int currentPage, int pageSize) {
        Page<AdminRole> page = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<AdminRole> queryWrapper = Wrappers.lambdaQuery();
        return this.adminRoleMapper.selectPage(page,queryWrapper);
    }

    public List<AdminPermission> findAllPermission(AdminUserParams adminUserParams) {
        LambdaQueryWrapper<AdminPermission> queryWrapper = Wrappers.lambdaQuery();
        return this.adminPermissionMapper.selectList(queryWrapper);
    }

    public void saveRole(AdminRole adminRole) {
        this.adminRoleMapper.insert(adminRole);
    }

    public void saveRolePermission(Integer roleId, List<Integer> permissionIdList) {
        for (Integer permission : permissionIdList) {
            AdminRolePermission adminRolePermission = new AdminRolePermission();
            adminRolePermission.setRoleId(roleId);
            adminRolePermission.setPermissionId(permission);
            this.adminRolePermissionMapper.insert(adminRolePermission);
        }
    }

    public AdminRole findRoleByRoleId(Integer roleId) {
        return this.adminRoleMapper.selectById(roleId);
    }

    public void updateRole(AdminRole adminRole) {
        this.adminRoleMapper.updateById(adminRole);
    }

    public void deleteRolePermissionByRoleId(Integer roleId) {
        LambdaQueryWrapper<AdminRolePermission> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AdminRolePermission::getRoleId,roleId);
        this.adminRolePermissionMapper.delete(queryWrapper);
    }

    public Page<AdminPermission> findPermissionPage(int currenPage, int pageSize) {
        Page<AdminPermission> page = new Page<>(currenPage,pageSize);
        LambdaQueryWrapper<AdminPermission> queryWrapper = Wrappers.lambdaQuery();
        return this.adminPermissionMapper.selectPage(page,queryWrapper);
    }

    public void savePermission(AdminPermission adminPermission) {
        this.adminPermissionMapper.insert(adminPermission);
    }

    public AdminPermission findPermissionIdById(Integer permissionId) {
        return this.adminPermissionMapper.selectById(permissionId);
    }

    public void updatePermission(AdminPermission adminPermission) {
        this.adminPermissionMapper.updateById(adminPermission);
    }

    public Page<AdminUser> findUserList(int currentPage, int pageSize) {
        Page<AdminUser> page = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<AdminUser> queryWrapper = Wrappers.lambdaQuery();
        return this.adminUserMapper.selectPage(page,queryWrapper);
    }

    public List<AdminRole> findRoleAll() {
        return this.adminRoleMapper.selectList(Wrappers.lambdaQuery());
    }

    public void saveUserRole(Long adminUserId, Integer roleId) {
        AdminUserRole adminUserRole = new AdminUserRole();
        adminUserRole.setUserId(adminUserId);
        adminUserRole.setRoleId(roleId);
    }

    public void saveUser(AdminUser adminUser) {
        this.adminUserMapper.insert(adminUser);
    }

    public AdminUser findUserById(Long id) {
        return adminUserMapper.selectById(id);
    }

    public List<Integer> findAdminRoleIdListByUserId(Long id) {
        LambdaQueryWrapper<AdminUserRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AdminUserRole::getUserId,id);
        queryWrapper.select(AdminUserRole::getRoleId);
        List<AdminUserRole> adminUserRoleList = this.adminUserRoleMapper.selectList(queryWrapper);
        List<Integer> roleIdList = adminUserRoleList.stream().map(AdminUserRole::getRoleId).collect(Collectors.toList());
        return roleIdList;
    }

    public void updateUser(AdminUser adminUser) {
        this.adminUserMapper.updateById(adminUser);
    }

    public void deleteUserRoleByUserId(Long userId) {
        LambdaQueryWrapper<AdminUserRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AdminUserRole::getUserId,userId);
        this.adminUserRoleMapper.delete(queryWrapper);
    }

    public Page<AdminMenu> findMenuPage(int currentPage, int pageSize) {
        Page<AdminMenu> page = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<AdminMenu> queryWrapper = Wrappers.lambdaQuery();
        return this.adminMenuMapper.selectPage(page,queryWrapper);
    }

    public List<AdminMenu> findMenuAll() {
        return adminMenuMapper.selectList(Wrappers.lambdaQuery());
    }

    public void saveMenu(AdminMenu adminMenu) {
        this.adminMenuMapper.insert(adminMenu);
    }

    public AdminMenu findMenuById(Long id) {
        return this.adminMenuMapper.selectById(id);
    }

    public void updateMenu(AdminMenu adminMenu) {
        this.adminMenuMapper.updateById(adminMenu);
    }

    public List<AdminMenu> findMenuListByRoleIds(List<Integer> roleIdList) {
        LambdaQueryWrapper<AdminRoleMenu> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(AdminRoleMenu::getRoleId,roleIdList);
        List<AdminRoleMenu> adminRoleMenus = adminRoleMenuMapper.selectList(queryWrapper);
        List<Integer> menuIdList = adminRoleMenus.stream().map(AdminRoleMenu::getMenuId).collect(Collectors.toList());

        LambdaQueryWrapper<AdminMenu> queryWrapper1 = Wrappers.lambdaQuery();
        queryWrapper1.in(AdminMenu::getId,menuIdList);
        queryWrapper1.orderByDesc(AdminMenu::getMenuSeq);//排序
        return this.adminMenuMapper.selectList(queryWrapper1);
    }
}
