package com.sft.service.impl;

import com.sft.db.SqlConnectionFactory;
import com.sft.model.Permission;
import com.sft.model.Role;
import com.sft.service.RolePermissionService;
import com.sft.util.DateUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class RolePermissionServiceImpl implements RolePermissionService {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public boolean addRole(Role role) {
        if (role == null) {
            return false;
        }
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sb = new StringBuffer();
        sb.append("insert into role (id,name,del_flag,remarks,create_by,create_date) values (?,?,?,?,?,?)");
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, role.getName());
            ps.setInt(3, role.getDel_flag() != 0 ? 1 : 0);
            ps.setString(4, role.getRemarks());
            ps.setString(5, role.getCreate_by());
            ps.setString(6, DateUtil.getCurDate());
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

    public List<Role> getRoles(String account) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Role> rolesList = new ArrayList<Role>();
        StringBuffer sb = new StringBuffer();

        sb.append("select r.id,r.name from role r,user_role ur,plat_user u where r.id = ur.role_id and ur.user_id = u.id and u.login_name = ?");
        sb.append(" and u.del_flag = 0 and r.del_flag = 0");
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, account);
            rs = ps.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getString("id"));
                role.setName(rs.getString("name"));
                rolesList.add(role);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return rolesList;
    }

    public boolean addPermission(Permission permission) {
        if (permission == null) {
            return false;
        }
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sb = new StringBuffer();
        sb.append("insert into permission (id,server_id,name,permission,create_by,create_date,remarks,del_flag) values (?,?,?,?,?,?,?,?)");
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, permission.getServer_id());
            ps.setString(3, permission.getName());
            ps.setString(4, permission.getPermission());
            ps.setString(5, permission.getCreate_by());
            ps.setString(6, DateUtil.getCurDate());
            ps.setString(7, permission.getRemarks());
            ps.setInt(8, permission.getDel_flag() != 0 ? 1 : 0);
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

    public List<String> getPermissions(String account) {
        StringBuffer sb = new StringBuffer();
        sb.append("select p.permission from role_permission rp,permission p where ");
        sb.append("rp.role_id = (select r.id from role r,user_role ur,plat_user u " +
                "where r.id = ur.role_id and ur.user_id = u.id and u.login_name = ? and u.del_flag = 0 and r.del_flag = 0)");
        sb.append(" and rp.permission_id = p.id and p.del_flag = 0");
        return getPermissions(sb.toString(), account);
    }

    public List<String> getRolePermissions(String roleId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select p.permission from role_permission rp,permission p where rp.role_id = ?");
        sb.append(" and rp.permission_id = p.id and p.del_flag = 0");
        return getPermissions(sb.toString(), roleId);
    }

    private List<String> getPermissions(String sql, String key) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> permissionsList = new ArrayList<String>();

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, key);
            rs = ps.executeQuery();
            while (rs.next()) {
                permissionsList.add(rs.getString("permission"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return permissionsList;
    }

}
