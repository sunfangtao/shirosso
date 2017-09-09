package com.sft.dao;

import com.sft.model.bean.RoleBean;

import java.util.List;

public interface RoleDao {

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
    public boolean updateUserRoles(String userId, List<String> roleIdList, String time, String by);
}
