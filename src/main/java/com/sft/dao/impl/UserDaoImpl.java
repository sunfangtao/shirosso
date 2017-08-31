package com.sft.dao.impl;

import com.sft.dao.UserDao;
import com.sft.db.SqlConnectionFactory;
import com.sft.model.UserModel;
import com.sft.util.DateUtil;
import com.sft.util.DefaultPasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserDaoImpl implements UserDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;
    @Resource
    private DefaultPasswordEncoder passwordEncoder;

    private UserModel getUser(String sql, String key) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserModel user = new UserModel();
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, key);
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

    public UserModel getUserByAccount(String account) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from plat_user where login_name = ?");
        sb.append(" and del_flag = 0");
        return getUser(sb.toString(), account);
    }

    public UserModel getUserById(String userId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from plat_user where id = ?");
        sb.append(" and del_flag = 0");
        return getUser(sb.toString(), userId);
    }

    public boolean updateUser(UserModel user) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sb = new StringBuffer();
        sb.append("update plat_user set ");
        if (user.getDel_flag() != 0) {
            sb.append(" del_flag = 1");
        }
        if (user.getPassword() != null) {
            sb.append(" password = '").append(user.getPassword()).append("'");
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
        if (user.getPhoto() != null) {
            sb.append(" and photo = '").append(user.getPhoto()).append("'");
        }
        sb.append(" where login_name = ?");

        UserModel changeUser = null;
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, user.getLogin_name());
            int result = ps.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, null);
        }
        return false;
    }

    public boolean addUser(UserModel user) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sb = new StringBuffer();
        sb.append("insert into plat_user (id,login_name,password,name,mobile,create_date,create_by,parent_id) values (?,?,?,?,?,?,?,?)");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, user.getLogin_name());
            ps.setString(3, passwordEncoder.encode(user.getPassword()));
            ps.setString(4, user.getName());
            ps.setString(5, user.getMobile());
            ps.setString(6, DateUtil.getCurDate());
            ps.setString(7, user.getCreate_by());
            ps.setString(8, user.getParent_id());

            int result = ps.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, null);
        }
        return false;
    }

    public boolean verifyUser(String account, String password) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserModel user = new UserModel();
        StringBuffer sb = new StringBuffer();

        sb.append("select id from plat_user where login_name = ? and password = ? and del_flag = 0");
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, account);
            ps.setString(2, password);
            rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return false;
    }

    public List<UserModel> getSubUserById(String userId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<UserModel> userList = new ArrayList<UserModel>();
        StringBuffer sb = new StringBuffer();

        sb.append("select * from plat_user where parent_id = ? and del_flag = 0");
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                UserModel user = new UserModel();
                user.setLogin_name(rs.getString("login_name"));
                user.setLogin_date(rs.getString("login_date"));
                user.setLogin_ip(rs.getString("login_ip"));
                user.setMobile(rs.getString("mobile"));
                user.setPhone(rs.getString("phone"));
                user.setEmail(rs.getString("email"));
                user.setName(rs.getString("name"));

                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return userList;
    }
}
