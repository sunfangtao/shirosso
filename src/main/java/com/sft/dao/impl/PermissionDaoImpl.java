package com.sft.dao.impl;

import com.sft.dao.PermissionDao;
import com.sft.db.SqlConnectionFactory;
import com.sft.model.bean.PermissionBean;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PermissionDaoImpl implements PermissionDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public List<PermissionBean> getUrlPermissions() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PermissionBean> permissionsList = new ArrayList<PermissionBean>();
        String sql = "select permission,url from permission where del_flag = 0";
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                PermissionBean permission = new PermissionBean();
                permission.setPermission(rs.getString("permission"));
                permission.setUrl(rs.getString("url"));
                permissionsList.add(permission);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return null;
    }

    public List<String> getPermissions(String userId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> permissionsList = new ArrayList<String>();

        StringBuffer sb = new StringBuffer();
        sb.append("select p.permission from role_permission rp,permission p where ");
        sb.append("rp.role_id = (select r.id from role r,user_role ur,plat_user u " +
                "where r.id = ur.role_id and ur.user_id = u.id and u.id = ? and u.del_flag = 0 and r.del_flag = 0)");
        sb.append(" and rp.permission_id = p.id and p.del_flag = 0");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, userId);
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

    public String getUrlByType(String type) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuffer sb = new StringBuffer();
        sb.append("select p.url,s.address from permission p,sub_server s where type = ? and p.del_flag = 0");
        sb.append(" and p.module_id = s.id and s.del_flag = 0");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, type);
            rs = ps.executeQuery();
            while (rs.next()) {
                return (rs.getString("address") + rs.getString("url"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return null;
    }
}
