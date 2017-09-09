package com.sft.service.impl;

import com.sft.dao.PermissionDao;
import com.sft.model.bean.PermissionBean;
import com.sft.service.PermissionService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionDao permissionDao;

    public List<PermissionBean> getUrlPermissions() {
        return permissionDao.getUrlPermissions();
    }

    public List<String> getPermissions(String userId) {
        return null;
    }
}
