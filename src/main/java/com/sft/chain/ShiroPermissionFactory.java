package com.sft.chain;

import com.sft.bean.UrlPermissionBean;
import com.sft.service.UserRolePermissionsInterface;
import org.apache.shiro.config.Ini;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

public class ShiroPermissionFactory extends ShiroFilterFactoryBean {

    public static String definition;

    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    private UserRolePermissionsInterface userRolePermissionsInterface;

    public void setUserRolePermissionsInterface(UserRolePermissionsInterface userRolePermissionsInterface) {
        this.userRolePermissionsInterface = userRolePermissionsInterface;
    }

    /**
     * 初始化设置过滤链
     */
    @Override
    public void setFilterChainDefinitions(String definitions) {
        definition = definitions;
        // 加载配置默认的过滤链
        Ini ini = new Ini();
        ini.load(definitions);
        Ini.Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection("");
        }

        List<UrlPermissionBean> permissions = userRolePermissionsInterface.getUrlPermissions();
        // 循环Resource的url,逐个添加到section中。section就是filterChainDefinitionMap,
        // 里面的键就是链接URL,值就是存在什么条件才能访问该链接
        if (permissions != null) {
            for (Iterator<UrlPermissionBean> it = permissions.iterator(); it.hasNext(); ) {
                UrlPermissionBean resource = it.next();
                // 如果不为空值添加到section中
                if (StringUtils.hasText(resource.url) && StringUtils.hasText(resource.permission)) {
                    if (!resource.url.startsWith("/")) {
                        resource.url = "/" + resource.url;
                    }
                    section.put(resource.url, MessageFormat.format(PREMISSION_STRING, resource.permission));
                }
            }
        }

        if (userRolePermissionsInterface.getAnonUrl() != null) {
            for (String url : userRolePermissionsInterface.getAnonUrl()) {
                section.put(url, "anon");
            }
        }
        section.put("/uController/**", "anon");
        section.put("/**", "user");
        this.setFilterChainDefinitionMap(section);
    }

}  