package com.sft.dao.impl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.sft.dao.RolePermissionDao;
import com.sft.db.SqlConnectionFactory;
import com.sft.model.Permission;
import com.sft.model.Role;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RolePermissionDaoImpl implements RolePermissionDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public boolean addRole(Role role) {
        String s = "";
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sb = new StringBuffer();
        sb.append("insert into role (id,name,del_flag,remarks,create_by,create_date) values (?,?,?,?,?,?)");
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, role.getId());
            ps.setString(2, role.getName());
            ps.setInt(3, role.getDel_flag());
            ps.setString(4, role.getRemarks());
            ps.setString(5, role.getCreate_by());
            ps.setString(6, role.getCreate_date());
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

    public boolean updateRole(Role role) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sb = new StringBuffer();
        sb.append("update role set ");
        if (role.getDel_flag() != 0) {
            sb.append(" del_flag = 1");
        } else {
            sb.append(" del_flag = 0");
        }
        if (role.getName() != null) {
            sb.append(" name = '").append(role.getName()).append("'");
        }
        if (role.getRemarks() != null) {
            sb.append(" and remarks = '").append(role.getRemarks()).append("'");
        }
        sb.append(" where id = ?");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, role.getId());
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

    public List<Role> getRoles(int page, int pageSize) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Role> rolesList = new ArrayList<Role>();
        StringBuffer sb = new StringBuffer();

        sb.append("select * from role limit ");
        sb.append((page-1) * pageSize).append(",").append(pageSize);

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getString("id"));
                role.setName(rs.getString("name"));
                role.setRemarks(rs.getString("remarks"));
                role.setCreate_by(rs.getString("create_by"));
                role.setCreate_date(rs.getString("create_date"));
                role.setDel_flag(rs.getInt("del_flag"));
                rolesList.add(role);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return rolesList;
    }

    public int getRoleCount() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuffer sb = new StringBuffer();

        sb.append("select count(1) as count from role");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
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

    public List<Role> getRoles(String userId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Role> rolesList = new ArrayList<Role>();
        StringBuffer sb = new StringBuffer();

        sb.append("select r.id,r.name,r.remarks from role r,user_role ur,plat_user u where r.id = ur.role_id and ur.user_id = u.id and u.id = ?");
        sb.append(" and u.del_flag = 0 and r.del_flag = 0");
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getString("id"));
                role.setName(rs.getString("name"));
                role.setRemarks(rs.getString("remarks"));
                rolesList.add(role);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return rolesList;
    }

    public boolean updateUserRoles(String userId, List<String> roleIdList, String time, String by) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Statement stm = null;
        List<String> oriRoleIdList = new ArrayList<String>();

        try {
            con = sqlConnectionFactory.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement("select role_id from user_role where user_id = ?");
            rs = ps.executeQuery();
            while (rs.next()) {
                oriRoleIdList.add(rs.getString("role_id"));
            }

            List<String> deleteList = (List<String>) CollectionUtils.subtract(oriRoleIdList, roleIdList);
            List<String> addList = (List<String>) CollectionUtils.subtract(roleIdList, oriRoleIdList);

            int deleteLength, addLength = 0;
            if (deleteList == null) {
                deleteList = new ArrayList<String>();
            }
            if (addList == null) {
                addList = new ArrayList<String>();
            }
            if ((deleteLength = deleteList.size()) == 0 && (addLength = addList.size()) == 0) {
                return true;
            }

            stm = con.createStatement();
            StringBuffer sb = new StringBuffer();
            // 增加新的角色
            sb.append("insert into user_role (user_id,role_id,create_by,create_date) values ");
            if (addLength > 0) {
                for (int i = 0; i < addLength; i++) {
                    sb.append("(").append(userId).append(",");
                    sb.append(roleIdList.get(i)).append(",").append(by).append(",").append(time).append(")");
                    if (i < addLength - 1) {
                        sb.append(",");
                    }
                }
                stm.addBatch(sb.toString());
            }

            if (deleteLength > 0) {
                // 删除角色
                sb = new StringBuffer();
                sb.append("delete from user_role ur,sys_user u where u.parent_id_set like %").append(userId).append("%");
                sb.append(" and u.id = ur.user_id and ur.role_id in (");
                for (int i = 0; i < deleteLength; i++) {
                    sb.append(deleteList.get(i));
                    if (i < deleteLength - 1) {
                        sb.append(",");
                    }
                }
                stm.addBatch(sb.toString());
            }
            int[] result = stm.executeBatch();
            con.commit();
            con.setAutoCommit(true);
            int length = result.length;
            int l = length;
            for (int i = 0; i < length; i++) {
                l--;
            }
            return l == 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stm != null)
                    stm.close();
            } catch (Exception e) {

            }
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return false;
    }

    public boolean updateRolePermission(String roleId, List<String> permissionId) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sb = new StringBuffer();
        sb.append("insert into role_permission (role_id,permission_id) values ");

        int length = permissionId.size();
        for (int i = 0; i < length; i++) {
            sb.append("(").append(roleId).append(",");
            sb.append(permissionId.get(i)).append(")");
            if (i < length - 1) {
                sb.append(",");
            }
        }
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
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

    public boolean addPermission(Permission permission) {
        if (permission == null) {
            return false;
        }
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sb = new StringBuffer();
        sb.append("insert into permission (id,module_id,name,permission,create_by,create_date,remarks,del_flag,url,type) values (?,?,?,?,?,?,?,?,?,?)");
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, permission.getId());
            ps.setString(2, permission.getModule_id());
            ps.setString(3, permission.getName());
            ps.setString(4, permission.getPermission());
            ps.setString(5, permission.getCreate_by());
            ps.setString(6, permission.getCreate_date());
            ps.setString(7, permission.getRemarks());
            ps.setInt(8, permission.getDel_flag() != 0 ? 1 : 0);
            ps.setString(9, permission.getUrl());
            ps.setString(10, permission.getType());
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

    public boolean updatePermission(Permission permission) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sb = new StringBuffer();
        sb.append("update permission set ");
        if (permission.getDel_flag() != 0) {
            sb.append(" del_flag = 1");
        }
        if (permission.getName() != null) {
            sb.append(" name = '").append(permission.getName()).append("'");
        }
        if (permission.getRemarks() != null) {
            sb.append(" and remarks = '").append(permission.getRemarks()).append("'");
        }
        if (permission.getType() != null) {
            sb.append(" and type = '").append(permission.getType()).append("'");
        }
        if (permission.getUrl() != null) {
            sb.append(" and url = '").append(permission.getUrl()).append("'");
        }
        if (permission.getUpdate_by() != null) {
            sb.append(" and update_by = '").append(permission.getUpdate_by()).append("'");
        }
        if (permission.getUpdate_date() != null) {
            sb.append(" and update_date = '").append(permission.getUpdate_date()).append("'");
        }
        if (permission.getPermission() != null) {
            sb.append(" and permission = '").append(permission.getPermission()).append("'");
        }
        sb.append(" where id = ?");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, permission.getId());
            int result = ps.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (MySQLIntegrityConstraintViolationException e) {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, null);
        }
        return false;
    }

    public List<String> getPermissions(String userId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select p.permission from role_permission rp,permission p where ");
        sb.append("rp.role_id = (select r.id from role r,user_role ur,plat_user u " +
                "where r.id = ur.role_id and ur.user_id = u.id and u.id = ? and u.del_flag = 0 and r.del_flag = 0)");
        sb.append(" and rp.permission_id = p.id and p.del_flag = 0");
        return getPermissions(sb.toString(), userId);
    }

    public List<Permission> getPermissions(int page, int pageSize) {
        return null;
    }

    public int getPermissionCount() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuffer sb = new StringBuffer();

        sb.append("select count(1) as count from permission");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
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

    public List<String> getRolePermissions(String roleId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select p.permission from role_permission rp,permission p where rp.role_id = ?");
        sb.append(" and rp.permission_id = p.id and p.del_flag = 0");
        return getPermissions(sb.toString(), roleId);
    }

    public List<Permission> getRolePermissionsList(String roleId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Permission> permissionsList = new ArrayList<Permission>();

        StringBuffer sb = new StringBuffer();
        sb.append("select p.permission,p.remarks,p.module_id,p.name,p.id from role_permission rp,permission p where rp.role_id = ?");
        sb.append(" and rp.permission_id = p.id and p.del_flag = 0");
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, roleId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Permission permission = new Permission();
                permission.setRemarks(rs.getString("remarks"));
                permission.setModule_id(rs.getString("module_id"));
                permission.setName(rs.getString("name"));
                permission.setId(rs.getString("id"));
                permission.setPermission(rs.getString("permission"));
                permissionsList.add(permission);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return permissionsList;
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

    public List<Permission> getUrlPermissions() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Permission> permissionsList = new ArrayList<Permission>();
        String sql = "select permission,url from permission where del_flag = 0";
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Permission permission = new Permission();
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
}
