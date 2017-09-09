package com.sft.service;

import com.sft.model.bean.PermissionBean;

import java.util.List;

public interface PermissionService {

    /**
     * 获取资源权限
     *
     * @return
     */
    public List<PermissionBean> getUrlPermissions();

    /**
     * 获取用户的权限
     *
     * @param userId
     * @return
     */
    public List<String> getPermissions(String userId);
}
