package com.sft.service;

import com.sft.bean.UrlPermissionBean;

import java.util.List;

public interface UserRolePermissionsInterface {

    /**
     * 获取用户唯一信息
     *
     * @param account
     * @return
     */
    public String getUserId(String account);

    /**
     * 获取用户的角色
     *
     * @param account
     * @return
     */
    public List<String> getRoles(String account);

    /**
     * 获取资源权限
     *
     * @return
     */
    public List<UrlPermissionBean> getUrlPermissions();

    /**
     * 获取用户的权限
     *
     * @param account
     * @return
     */
    public List<String> getPermissions(String account);

}
