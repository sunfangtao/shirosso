package com.sft.dao.impl;

import com.sft.dao.ServiceDao;
import com.sft.db.SqlConnectionFactory;
import com.sft.model.bean.ModuleBean;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ServiceDaoImpl implements ServiceDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public List<ModuleBean> getAllModule() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ModuleBean> moduleList = new ArrayList<ModuleBean>();

        StringBuffer sb = new StringBuffer();
        sb.append("select s.*,u.name as create_name from sub_server s,sys_user u where s.create_by = u.id");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());

            rs = ps.executeQuery();
            while (rs.next()) {
                ModuleBean module = new ModuleBean();
                module.setAddress(rs.getString("address"));
                module.setCreate_by(rs.getString("create_by"));
                module.setCreate_name(rs.getString("create_name"));
                module.setCreate_date(rs.getString("create_date"));
                module.setDel_flag(rs.getInt("del_flag"));
                module.setId(rs.getString("id"));
                module.setName(rs.getString("name"));
                moduleList.add(module);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return moduleList;
    }
}
