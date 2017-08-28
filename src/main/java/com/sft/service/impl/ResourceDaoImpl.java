package com.sft.service.impl;

import com.sft.bean.ResourcePermission;
import com.sft.db.SqlConnectionFactory;
import com.sft.model.UserModel;
import com.sft.service.ResourceService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ResourceDaoImpl implements ResourceService {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public List<ResourcePermission> getAllResourcePer() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ResourcePermission> resourcePermissionList = new ArrayList<ResourcePermission>();
        StringBuffer sb = new StringBuffer();

        sb.append("select id,url,permission from resource_permission where del_flag = 0");
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                ResourcePermission resourcePermission = new ResourcePermission();
                resourcePermission.setId(rs.getString("id"));
                resourcePermission.setUrl(rs.getString("url"));
                resourcePermission.setPermission(rs.getString("permission"));
                resourcePermissionList.add(resourcePermission);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }

        return resourcePermissionList;
    }

    public boolean addResourcePer(ResourcePermission resource) {
        if (resource == null) {
            return false;
        }
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sb = new StringBuffer();
        sb.append("insert into resource_permission (permission,url,del_flag) values (?,?,?)");
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, resource.getPermission());
            ps.setString(2, resource.getUrl());
            ps.setInt(3, resource.getDel_flag() != 0 ? 1 : 0);
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

    public boolean updateResourcePer(ResourcePermission resourcePermission) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sb = new StringBuffer();
        sb.append("update resource_permission set ");
        if (resourcePermission.getDel_flag() != 0) {
            sb.append(" del_flag = 1");
        }
        if (resourcePermission.getPermission() != null) {
            sb.append(" and permission = '").append(resourcePermission).append("'");
        }
        if (resourcePermission.getUrl() != null) {
            sb.append(" and url = '").append(resourcePermission.getUrl()).append("'");
        }
        sb.append(" where id = ?");

        UserModel changeUser = null;
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, resourcePermission.getId());
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
}
