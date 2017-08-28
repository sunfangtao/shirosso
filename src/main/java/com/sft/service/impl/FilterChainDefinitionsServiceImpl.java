package com.sft.service.impl;

import com.sft.bean.ResourcePermission;
import com.sft.chain.ShiroPermissionFactory;
import com.sft.service.FilterChainDefinitionsService;
import com.sft.service.ResourceService;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.sft.chain.ShiroPermissionFactory.PREMISSION_STRING;

@Repository
public class FilterChainDefinitionsServiceImpl implements FilterChainDefinitionsService {

    @Autowired
    private ShiroPermissionFactory permissFactory;
    @Resource
    private ResourceService resourceDao;

    public void reloadFilterChains() {

        synchronized (permissFactory) {   // 强制同步，控制线程安全
            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) permissFactory.getObject();
                PathMatchingFilterChainResolver resolver = (PathMatchingFilterChainResolver) shiroFilter
                        .getFilterChainResolver();
                // 过滤管理器
                DefaultFilterChainManager manager = (DefaultFilterChainManager) resolver.getFilterChainManager();
                // 清除权限配置
                // manager.getFilterChains().clear();
                permissFactory.getFilterChainDefinitionMap().clear();
                // 重新设置权限
                permissFactory.setFilterChainDefinitions(ShiroPermissionFactory.definition);// 传入配置中的filterchains

                List<ResourcePermission> permissions = resourceDao.getAllResourcePer();
                // 循环Resource的url,逐个添加到section中。section就是filterChainDefinitionMap,
                // 里面的键就是链接URL,值就是存在什么条件才能访问该链接

                Map<String, String> map = new HashMap<String, String>();
                for (Iterator<ResourcePermission> it = permissions.iterator(); it.hasNext(); ) {
                    ResourcePermission resource = it.next();
                    // 如果不为空值添加到section中
                    if (StringUtils.hasText(resource.getUrl()) && StringUtils.hasText(resource.getPermission())) {
                        manager.createChain(resource.getUrl(), MessageFormat.format(PREMISSION_STRING, resource.getPermission()));
                        map.put(resource.getUrl(), MessageFormat.format(PREMISSION_STRING, resource.getPermission()));
                    }
                }
                permissFactory.setFilterChainDefinitionMap(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
