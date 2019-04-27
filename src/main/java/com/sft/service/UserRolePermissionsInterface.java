package com.sft.service;

import com.sft.bean.UrlPermissionBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * 指定不需要认证就可以访问的接口
     *
     * @return
     */
    public List<String> getAnonUrl();

    /**
     * 用户没有认证或没有权限的处理
     *
     * @param ex
     */
    public String doResolveException(Exception ex);
}
