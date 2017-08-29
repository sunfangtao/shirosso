package com.sft.service;

import com.sft.model.UserModel;

public interface UserService {

    /**
     * 获取用户信息
     *
     * @param account
     * @return
     */
    public UserModel getUserByAccount(String account);

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    public UserModel updateUser(UserModel user);

    /**
     * 添加新用户
     *
     * @param user
     * @return
     */
    public UserModel addUser(UserModel user);

    /**
     * 清除用户的授权信息
     *
     * @param account
     * @return
     */
    public boolean clearAuthorizationInfo(String account);
}
