package com.sft.service.impl;

import com.sft.dao.UserDao;
import com.sft.model.UserModel;
import com.sft.service.UserService;
import com.sft.util.DateUtil;
import com.sft.util.DefaultPasswordEncoder;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class UserServiceImpl implements UserService {

    @Resource
    private DefaultPasswordEncoder passwordEncoder;
    @Resource
    private UserDao userDao;

    public UserModel getUserById(String userId) {
        return userDao.getUserById(userId);
    }

    public UserModel getUserByAccount(String account) {
        return userDao.getUserByAccount(account);
    }

    public boolean updateUser(UserModel user, String by) {
        // userId ="" 表示用户登录更新数据
        if (user.getDel_flag() != 0) {
            user.setDel_flag(1);
        }
        if (by == null) {
            throw new RuntimeException("更新用户没有更新者的ID！");
        } else if (by.length() > 0) {
            user.setUpdate_by(by);
            user.setUpdate_date(DateUtil.getCurDate());
        }
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userDao.updateUser(user);
    }

    public boolean addUser(UserModel user) {
        if (!StringUtils.hasText(user.getParent_id())) {
            return false;
        }
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userDao.addUser(user);
    }

    public boolean verifyUser(String account, String password) {
        return userDao.verifyUser(account, password);
    }

    public List<UserModel> getSubUserById(String userId) {
        if (StringUtils.hasText(userId)) {
            return userDao.getSubUserById(userId);
        }
        return null;
    }
}
