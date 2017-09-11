package com.sft.dao;

import com.sft.model.UserModel;

import java.util.List;
import java.util.Map;

public interface UserDao {

    /**
     * 获取用户信息
     *
     * @param account
     * @return
     */
    public UserModel getUserByAccount(String account);

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    public UserModel getUserById(String userId);

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    public boolean updateUser(UserModel user);

    /**
     * 添加新用户
     *
     * @param user
     * @return
     */
    public boolean addUser(UserModel user);

    /**
     * 验证用户密码
     *
     * @param account
     * @param password
     * @return
     */
    public boolean verifyUser(String account, String password);

    /**
     * 获取用户的子用户
     *
     * @param userId
     * @return
     */
    public List<UserModel> getSubUserById(Map<String, String> whereMap, String userId, int page, int pageSize);

    /**
     * 获取用户的子用户
     *
     * @param userId
     * @return
     */
    public List<UserModel> getAllSubUserById(Map<String, String> whereMap, String userId, int page, int pageSize);

    public int getSubUserCount(Map<String, String> whereMap, String userId);

    public int getAllSubUserCount(Map<String, String> whereMap, String userId);
}
