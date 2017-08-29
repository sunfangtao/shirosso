package com.sft.service.impl;

import com.sft.db.SqlConnectionFactory;
import com.sft.model.UserModel;
import com.sft.service.UserService;
import com.sft.util.DateUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Repository
public class UserServiceImpl implements UserService {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;
    @Resource
    private EhCacheManager shiroCacheManager;

    public UserModel getUserByAccount(String account) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserModel user = new UserModel();
        StringBuffer sb = new StringBuffer();

        sb.append("select * from plat_user where login_name = ?");
        sb.append(" and del_flag = 0");
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, account);
            rs = ps.executeQuery();
            while (rs.next()) {
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setMobile(rs.getString("mobile"));
                user.setLogin_ip(rs.getString("login_ip"));
                user.setLogin_date(rs.getString("login_date"));
                user.setLogin_name(rs.getString("login_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return user;
    }

    public UserModel updateUser(UserModel user) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sb = new StringBuffer();
        sb.append("update plat_user set ");
        if (user.getDel_flag() != 0) {
            sb.append(" del_flag = 1");
        }
        if (user.getEmail() != null) {
            sb.append(" and email = '").append(user.getEmail()).append("'");
        }
        if (user.getLogin_date() != null) {
            sb.append(" and login_date = '").append(user.getLogin_date()).append("'");
        }
        if (user.getLogin_ip() != null) {
            sb.append(" and login_ip = '").append(user.getLogin_ip()).append("'");
        }
        if (user.getMobile() != null) {
            sb.append(" and mobile = '").append(user.getMobile()).append("'");
        }
        if (user.getName() != null) {
            sb.append(" and name = '").append(user.getName()).append("'");
        }
        if (user.getPhone() != null) {
            sb.append(" and phone = '").append(user.getPhone()).append("'");
        }
        if (user.getRemarks() != null) {
            sb.append(" and remarks = '").append(user.getRemarks()).append("'");
        }
        if (user.getUpdate_by() != null) {
            sb.append(" and update_by = '").append(user.getUpdate_by()).append("'");
        }
        if (user.getUpdate_date() != null) {
            sb.append(" and update_date = '").append(user.getUpdate_date()).append("'");
        }
        sb.append(" where login_name = ?");

        UserModel changeUser = null;
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, user.getLogin_name());
            int result = ps.executeUpdate();
            if (result > 0) {
                changeUser = getUserByAccount(user.getLogin_name());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, null);
        }
        return changeUser;
    }

    public UserModel addUser(UserModel user) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sb = new StringBuffer();
        sb.append("insert into plat_user (id,login_name,password,name,mobile,create_date,create_by) values (?,?,?,?,?,?,?)");

        UserModel changeUser = null;
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, user.getLogin_name());
            ps.setString(3, user.getPassword());//md5
            ps.setString(4, user.getName());
            ps.setString(5, user.getMobile());
            ps.setString(6, DateUtil.getCurDate());
            ps.setString(7, user.getCreate_by());

            int result = ps.executeUpdate();
            if (result > 0) {
                changeUser = getUserByAccount(user.getLogin_name());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, null);
        }
        return changeUser;
    }

    public boolean clearAuthorizationInfo(String account) {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            Cache<Object, Object> cache = shiroCacheManager.getCache("authorizationCache");
            cache.remove(account);
            return true;
        }
        return false;
    }
}
