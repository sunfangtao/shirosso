package com.sft.dao.impl;

import com.sft.dao.UserDao;
import com.sft.db.SqlConnectionFactory;
import com.sft.model.UserModel;
import com.sft.util.DateUtil;
import com.sft.util.DefaultPasswordEncoder;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                user.setParent_id(rs.getString("parent_id"));
                user.setParent_id_set(rs.getString("parent_id_set"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
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
        sb.append("select * from sys_user where login_name = ?");
        sb.append(" and del_flag = 0");
        return getUser(sb.toString(), account);
    }

    public UserModel getUserById(String userId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from sys_user where id = ?");
        sb.append(" and del_flag = 0");
        return getUser(sb.toString(), userId);
    }

    public boolean updateUser(UserModel user) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sb = new StringBuffer();
        sb.append("update sys_user set");
        if (user.getDel_flag() != 0) {
            sb.append(" del_flag = 1");
        } else {
            sb.append(" del_flag = 0");
        }
        if (user.getPassword() != null) {
            sb.append(", password = '").append(user.getPassword()).append("'");
        }
        if (user.getEmail() != null) {
            sb.append(", email = '").append(user.getEmail()).append("'");
        }
        if (user.getLogin_date() != null) {
            sb.append(", login_date = '").append(user.getLogin_date()).append("'");
        }
        if (user.getLogin_ip() != null) {
            sb.append(", login_ip = '").append(user.getLogin_ip()).append("'");
        }
        if (user.getName() != null) {
            sb.append(", name = '").append(user.getName()).append("'");
        }
        if (user.getPhone() != null) {
            sb.append(", phone = '").append(user.getPhone()).append("'");
        }
        if (user.getRemarks() != null) {
            sb.append(", remarks = '").append(user.getRemarks()).append("'");
        }
        if (user.getUpdate_by() != null) {
            sb.append(", update_by = '").append(user.getUpdate_by()).append("'");
        }
        if (user.getUpdate_date() != null) {
            sb.append(", update_date = '").append(user.getUpdate_date()).append("'");
        }
        if (user.getPhoto() != null) {
            sb.append(", photo = '").append(user.getPhoto()).append("'");
        }
        sb.append(" where login_name = ?");

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
        sb.append("insert into sys_user (id,login_name,password,name,create_date,create_by,parent_id,parent_id_set) values (?,?,?,?,?,?,?,?)");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, user.getLogin_name());
            ps.setString(3, passwordEncoder.encode(user.getPassword()));
            ps.setString(4, user.getName());
            ps.setString(5, DateUtil.getCurDate());
            ps.setString(6, user.getCreate_by());
            ps.setString(7, user.getParent_id());
            ps.setString(8, user.getParent_id_set());

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
        StringBuffer sb = new StringBuffer();

        sb.append("select id from sys_user where login_name = ? and password = ? and del_flag = 0");
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

    private List<UserModel> getUserList(Map<String, String> whereMap, int page, int pageSize, StringBuffer sb, String key) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<UserModel> userList = new ArrayList<UserModel>();

        if (whereMap != null) {
            String name = whereMap.get("name");
            if (StringUtils.hasText(name)) {
                sb.append(" and name like %").append(name).append("%");
            }
            String phone = whereMap.get("phone");
            if (StringUtils.hasText(phone)) {
                sb.append(" and phone like %").append(phone).append("%");
            }
        }
        if (page > 0 && pageSize > 0) {
            sb.append(" order by create_date desc limit ");
            sb.append((page - 1) * pageSize).append(",").append(pageSize);
        }

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            if (key != null)
                ps.setString(1, key);
            rs = ps.executeQuery();
            while (rs.next()) {
                UserModel user = new UserModel();
                user.setId(rs.getString("id"));
                user.setParent_id(rs.getString("parent_id"));
                user.setParent_id_set(rs.getString("parent_id_set"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setLogin_ip(rs.getString("login_ip"));
                user.setLogin_date(rs.getString("login_date"));
                user.setLogin_name(rs.getString("login_name"));

                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return userList;
    }

    public List<UserModel> getSubUserById(Map<String, String> whereMap, String userId, int page, int pageSize) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from sys_user where parent_id = ? and del_flag = 0");
        return getUserList(whereMap, page, pageSize, sb, userId);
    }

    public List<UserModel> getAllSubUserById(Map<String, String> whereMap, String userId, int page, int pageSize) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from sys_user where parent_id_set like %");
        sb.append(userId);
        sb.append("% and del_flag = 0");

        return getUserList(whereMap, page, pageSize, sb, null);
    }

    public int getSubUserCount(Map<String, String> whereMap, String userId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select count(1) as count from sys_user where del_flag = 0 and parent_id = ?");

        return getUserCount(whereMap, userId, sb);
    }

    public int getAllSubUserCount(Map<String, String> whereMap, String userId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from sys_user where parent_id_set like %\" + userId + \"% and del_flag = 0");

        return getUserCount(whereMap, null, sb);
    }

    public int getUserCount(Map<String, String> whereMap, String userId, StringBuffer sb) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        if (whereMap != null) {
            String name = whereMap.get("name");
            if (StringUtils.hasText(name)) {
                sb.append(" and name like %").append(name).append("%");
            }
            String phone = whereMap.get("phone");
            if (StringUtils.hasText(phone)) {
                sb.append(" and phone like %").append(phone).append("%");
            }
        }
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            if (userId != null)
                ps.setString(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return 0;

    }
}
