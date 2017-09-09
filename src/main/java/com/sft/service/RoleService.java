package com.sft.service;

import com.sft.model.bean.RoleBean;

import java.util.List;

public interface RoleService {

    /**
     * 获取用户的角色
     *
     * @param userId
     * @return
     */
    public List<RoleBean> getRoles(String userId);

    /**
     * 更新用户的角色
     *
     * @param userId
     * @param roleIdList
     * @param by
     * @return
     */
    public boolean updateUserRoles(String userId, List<String> roleIdList, String by);
}
