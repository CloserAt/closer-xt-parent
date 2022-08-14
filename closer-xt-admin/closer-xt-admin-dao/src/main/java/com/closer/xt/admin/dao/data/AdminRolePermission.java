package com.closer.xt.admin.dao.data;

import lombok.Data;

@Data
public class AdminRolePermission {

    private Long id;

    private Integer roleId;

    private Integer permissionId;
}
