package com.sft.chain;

import com.sft.model.bean.PermissionBean;
import com.sft.service.PermissionService;
import org.apache.shiro.config.Ini;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

public class ShiroPermissionFactory extends ShiroFilterFactoryBean {

    @Autowired
    private PermissionService permissionService;

    public static String definition;

    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    /**
     * 初始化设置过滤链
     */
    @Override
    public void setFilterChainDefinitions(String definitions) {
        definition = definitions;
        // 加载配置默认的过滤链
        Ini ini = new Ini();
        ini.load(definitions);
        Ini.Section section = ini.getSection("urls");
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection("");
        }

        List<PermissionBean> permissions = permissionService.getUrlPermissions();
        // 循环Resource的url,逐个添加到section中。section就是filterChainDefinitionMap,
        // 里面的键就是链接URL,值就是存在什么条件才能访问该链接
        if (permissions != null) {
            for (Iterator<PermissionBean> it = permissions.iterator(); it.hasNext(); ) {
                PermissionBean resource = it.next();
                // 如果不为空值添加到section中
                if (StringUtils.hasText(resource.getUrl()) && StringUtils.hasText(resource.getPermission())) {
                    section.put(resource.getUrl(), MessageFormat.format(PREMISSION_STRING, resource.getPermission()));
                }
            }
        }
        section.put("/**", "user");
        this.setFilterChainDefinitionMap(section);
    }

}  