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
}
