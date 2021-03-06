package com.sft.controller;

import com.sft.service.FilterChainDefinitionsService;
import com.sft.util.SendAppJSONUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CacheUpdateController {

    @Resource
    private FilterChainDefinitionsService filterChainDefinitionsService;
    @Resource
    private EhCacheManager shiroCacheManager;

    /**
     * shiro认证授权缓存
     *
     * @param req
     * @param res
     */
    @RequestMapping("shiroCache")
    public void shiroCache(HttpServletRequest req, HttpServletResponse res) {
        try {
            String account = req.getParameter("account");
            clearAuthorizationInfo(account);
            String returnJson = SendAppJSONUtil.getNormalString("授权信息刷新成功!");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 资源权限缓存
     *
     * @param req
     * @param res
     */
    @RequestMapping("resourceCache")
    public void resourceCache(HttpServletRequest req, HttpServletResponse res) {
        try {
            filterChainDefinitionsService.reloadFilterChains();
            String returnJson = SendAppJSONUtil.getNormalString("资源权限更新成功!");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除用户的授权信息
     */
    private void clearAuthorizationInfo(String userName) {
        if (userName != null) {
            Cache<Object, Object> cache = shiroCacheManager.getCache("authorizationCache");
            for (Object key : cache.keys()) {
                if (key instanceof SimplePrincipalCollection) {
                    SimplePrincipalCollection collection = (SimplePrincipalCollection) key;
                    if (userName.equals(collection.getPrimaryPrincipal().toString())) {
                        cache.remove(collection);
                        break;
                    }
                }
            }
        } else {
            Cache<Object, Object> cache = shiroCacheManager.getCache("authorizationCache");
            cache.clear();
        }
    }

}
