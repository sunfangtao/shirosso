package com.sft.service.impl;

import com.sft.dao.RolePermissionDao;
import com.sft.model.Permission;
import com.sft.model.Role;
import com.sft.service.RolePermissionService;
import com.sft.util.DateUtil;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Repository
public class RolePermissionServiceImpl implements RolePermissionService {

    @Resource
    private RolePermissionDao rolePermissionDao;

    public boolean addRole(Role role) {
        if (role == null) {
            return false;
        }
        if (role.getDel_flag() != 0) {
            role.setDel_flag(1);
        }
        role.setId(UUID.randomUUID().toString());
        role.setCreate_date(DateUtil.getCurDate());

        return rolePermissionDao.addRole(role);
    }

    public List<Role> getRoles(String userId) {
        return rolePermissionDao.getRoles(userId);
    }

    public List<Role> getRoles(int page, int pageSize) {
        return rolePermissionDao.getRoles(page, pageSize);
    }

    public int getRoleCount() {
        return rolePermissionDao.getRoleCount();
    }

    public boolean updateRole(Role role) {
        if (role == null) {
            return false;
        }
        if (StringUtils.hasText(role.getId())) {
            return rolePermissionDao.updateRole(role);
        }
        return false;
    }

    public boolean updateUserRoles(String userId, List<String> roleIdList, String by) {
        if (!StringUtils.hasText(userId)) {
            return false;
        }
        if (roleIdList == null || roleIdList.size() == 0) {
            return false;
        }
        return rolePermissionDao.updateUserRoles(userId, roleIdList, DateUtil.getCurDate(), by);
    }

    public boolean updateRolePermission(String roleId, List<String> permissionId) {
        if (StringUtils.hasText(roleId) && permissionId != null && permissionId.size() > 0) {
            return rolePermissionDao.updateRolePermission(roleId, permissionId);
        }
        return false;
    }

    public boolean addPermission(Permission permission) {
        if (permission == null) {
            return false;
        }
        if (permission.getDel_flag() != 0) {
            permission.setDel_flag(1);
        }
        permission.setId(UUID.randomUUID().toString());
        permission.setCreate_date(DateUtil.getCurDate());

        return rolePermissionDao.addPermission(permission);
    }

    public List<String> getPermissions(String userId) {
        if (StringUtils.hasText(userId)) {
            return rolePermissionDao.getPermissions(userId);
        }
        return null;
    }

    public List<Permission> getPermissions(int page, int pageSize) {
        return rolePermissionDao.getPermissions(page, pageSize);
    }

    public int getPermissionCount() {
        return rolePermissionDao.getPermissionCount();
    }

    public List<String> getRolePermissions(String roleId) {
        if (StringUtils.hasText(roleId)) {
            return rolePermissionDao.getRolePermissions(roleId);
        }
        return null;
    }

    public List<Permission> getRolePermissionsList(String roleId) {
        if (StringUtils.hasText(roleId)) {
            return rolePermissionDao.getRolePermissionsList(roleId);
        }
        return null;
    }

    public List<Permission> getUrlPermissions() {
        return rolePermissionDao.getUrlPermissions();
    }

}
