package com.sft.dao;

import com.sft.model.Permission;
import com.sft.model.Role;

import java.util.List;

public interface RolePermissionDao {

    /**
     * 新建角色
     *
     * @param role
     * @return
     */
    public boolean addRole(Role role);

    /**
     * 更新角色
     *
     * @param role
     * @return
     */
    public boolean updateRole(Role role);

    /**
     * 获取用户的角色
     *
     * @param userId
     * @return
     */
    public List<Role> getRoles(String userId);

    /**
     * 更新用户角色
     *
     * @param userId
     * @param roleIdList
     * @return
     */
    public boolean updateUserRoles(String userId, List<String> roleIdList, String time, String by);

    /**
     * 更新角色权限
     *
     * @param roleId
     * @param permissionId
     * @return
     */
    public boolean updateRolePermission(String roleId, List<String> permissionId);

    /**
     * 新建权限
     *
     * @param permission
     * @return
     */
    public boolean addPermission(Permission permission);

    /**
     * 更新权限
     *
     * @param permission
     * @return
     */
    public boolean updatePermission(Permission permission);

    /**
     * 获取用户的权限
     *
     * @param userId
     * @return
     */
    public List<String> getPermissions(String userId);

    /**
     * 获取角色的权限
     *
     * @param roleId
     * @return
     */
    public List<String> getRolePermissions(String roleId);

    /**
     * 获取角色的权限
     *
     * @param roleId
     * @return
     */
    public List<Permission> getRolePermissionsList(String roleId);

    /**
     * 获取资源权限
     *
     * @return
     */
    public List<Permission> getUrlPermissions();
}