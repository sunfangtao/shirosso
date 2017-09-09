package com.sft.dao.impl;

import com.sft.dao.RoleDao;
import com.sft.db.SqlConnectionFactory;
import com.sft.model.bean.RoleBean;
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
public class RoleDaoImpl implements RoleDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;
    
    public List<RoleBean> getRoles(String userId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<RoleBean> rolesList = new ArrayList<RoleBean>();
        StringBuffer sb = new StringBuffer();

        sb.append("select r.*,u.name as create_name from role r,user_role ur,sys_user u where r.id = ur.role_id and ur.user_id = u.id and u.id = ?");
        sb.append(" and u.del_flag = 0 and r.del_flag = 0");
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                RoleBean role = new RoleBean();
                role.setId(rs.getString("id"));
                role.setName(rs.getString("name"));
                role.setRemarks(rs.getString("remarks"));
                role.setCreate_by(rs.getString("create_by"));
                role.setCreate_date(rs.getString("create_date"));
                role.setDel_flag(rs.getInt("del_flag"));
                role.setCreate_name(rs.getString("create_name"));
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

}
