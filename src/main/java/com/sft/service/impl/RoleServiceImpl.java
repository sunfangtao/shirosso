package com.sft.service.impl;

import com.sft.dao.RoleDao;
import com.sft.model.bean.RoleBean;
import com.sft.service.RoleService;
import com.sft.util.DateUtil;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleDao roleDao;

    public List<RoleBean> getRoles(String userId) {
        return roleDao.getRoles(userId);
    }


    public boolean updateUserRoles(String userId, List<String> roleIdList, String by) {
        if (!StringUtils.hasText(userId)) {
            return false;
        }
        if (roleIdList == null || roleIdList.size() == 0) {
            return false;
        }
        return roleDao.updateUserRoles(userId, roleIdList, DateUtil.getCurDate(), by);
    }
}
