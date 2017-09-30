package com.sft.service.impl;

import com.sft.dao.PermissionDao;
import com.sft.model.bean.PermissionBean;
import com.sft.service.PermissionService;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class PermissionServiceImpl implements PermissionService {

    public static Map<String, String> urlMap = new TreeMap<String, String>();

    @Resource
    private PermissionDao permissionDao;

    public List<PermissionBean> getUrlPermissions() {
        return permissionDao.getUrlPermissions();
    }

    public List<String> getPermissions(String userId) {
        return permissionDao.getPermissions(userId);
    }

    public String getUrlByType(String type) {
        String url = urlMap.get(type);
        if (StringUtils.hasText(url)) {
            return url;
        }
        url = permissionDao.getUrlByType(type);
        urlMap.put(type, url);
        return url;
    }

    public void updateUrlByType(String type, String url) {
        urlMap.put(type, url);
    }
}
