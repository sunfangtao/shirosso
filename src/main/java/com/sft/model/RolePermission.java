package com.sft.model;

public class RolePermission {
    private String role_id;//'角色编号',
    private String permission_id;//'权限编号',

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getPermission_id() {
        return permission_id;
    }

    public void setPermission_id(String permission_id) {
        this.permission_id = permission_id;
    }
}