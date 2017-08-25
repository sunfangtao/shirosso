package com.sft.service;

import com.sft.model.Permission;
import com.sft.model.Role;

import java.util.List;

public interface RolePermissionService {

    /**
     * 新建角色
     *
     * @param role
     * @return
     */
    public boolean addRole(Role role);

    /**
     * 获取用户的角色
     *
     * @param account
     * @return
     */
    public List<Role> getRoles(String account);

    /**
     * 新建权限
     *
     * @param permission
     * @return
     */
    public boolean addPermission(Permission permission);

    /**
     * 获取用户的权限
     *
     * @param account
     * @return
     */
    public List<String> getPermissions(String account);

    /**
     * 获取角色的权限
     *
     * @param roleId
     * @return
     */
    public List<String> getRolePermissions(String roleId);

}
