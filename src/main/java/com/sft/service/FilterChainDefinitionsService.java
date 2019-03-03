package com.sft.service;

import com.sft.bean.UrlPermissionBean;
import com.sft.chain.ShiroPermissionFactory;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.sft.chain.ShiroPermissionFactory.PREMISSION_STRING;

public class FilterChainDefinitionsService {

    @Autowired
    private ShiroPermissionFactory permissionFactory;

    private UserRolePermissionsInterface userRolePermissionsInterface;

    public void setUserRolePermissionsInterface(UserRolePermissionsInterface userRolePermissionsInterface) {
        this.userRolePermissionsInterface = userRolePermissionsInterface;
    }

    public void reloadFilterChains() {

        synchronized (permissionFactory) { // 强制同步，控制线程安全
            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) permissionFactory.getObject();
                PathMatchingFilterChainResolver resolver = (PathMatchingFilterChainResolver) shiroFilter
                        .getFilterChainResolver();
                // 过滤管理器
                DefaultFilterChainManager manager = (DefaultFilterChainManager) resolver.getFilterChainManager();
                // 清除权限配置
                //manager.getFilterChains().clear();
                permissionFactory.getFilterChainDefinitionMap().clear();
                // 重新设置权限
                permissionFactory.setFilterChainDefinitions(ShiroPermissionFactory.definition);// 传入配置中的filterchains

                List<UrlPermissionBean> permissions = userRolePermissionsInterface.getUrlPermissions();
                // 循环Resource的url,逐个添加到section中。section就是filterChainDefinitionMap,
                // 里面的键就是链接URL,值就是存在什么条件才能访问该链接

                Map<String, String> map = new HashMap<String, String>();
                if (permissions != null) {
                    for (Iterator<UrlPermissionBean> it = permissions.iterator(); it.hasNext(); ) {
                        UrlPermissionBean resource = it.next();
                        // 如果不为空值添加到section中
                        if (StringUtils.hasText(resource.url) && StringUtils.hasText(resource.permission)) {
                            manager.createChain(resource.url, MessageFormat.format(PREMISSION_STRING, resource.permission));
                            if (!resource.url.startsWith("/")) {
                                resource.url = "/" + resource.url;
                            }
                            map.put(resource.url, MessageFormat.format(PREMISSION_STRING, resource.permission));
                        }
                    }
                }
                permissionFactory.setFilterChainDefinitionMap(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
