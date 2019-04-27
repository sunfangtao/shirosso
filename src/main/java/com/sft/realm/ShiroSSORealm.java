package com.sft.realm;

import com.sft.service.UserRolePermissionsInterface;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import javax.annotation.Resource;
import java.util.List;

public class ShiroSSORealm extends CasRealm {

    private static Logger logger = Logger.getLogger(ShiroSSORealm.class);

    @Resource
    private EhCacheManager shiroCacheManager;

    private UserRolePermissionsInterface userRolePermissionsInterface;

    public void setUserRolePermissionsInterface(UserRolePermissionsInterface userRolePermissionsInterface) {
        this.userRolePermissionsInterface = userRolePermissionsInterface;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        AuthenticationInfo authenticationInfo = super.doGetAuthenticationInfo(token);

        // 用户登录后清空用户权限，防止修改权限后关闭浏览器重新登录后权限没有修改的问题
        Cache<Object, Object> cache = shiroCacheManager.getCache("authorizationCache");
        for (Object key : cache.keys()) {
            if (key instanceof SimplePrincipalCollection) {
                SimplePrincipalCollection collection = (SimplePrincipalCollection) key;
                if (authenticationInfo.getPrincipals().getPrimaryPrincipal().equals(collection.getPrimaryPrincipal())) {
                    cache.remove(collection);
                    break;
                }
            }
        }

        return authenticationInfo;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        // TODO 获取用户的唯一信息
        String account = (String) principalCollection.getPrimaryPrincipal();
        String userId = userRolePermissionsInterface.getUserId(account);
        SimpleAuthorizationInfo authorizationInfo = null;
        if (authorizationInfo == null) {
            authorizationInfo = new SimpleAuthorizationInfo();
            List<String> permissions = userRolePermissionsInterface.getPermissions(userId);
            if (permissions != null) {
                authorizationInfo.addStringPermissions(permissions);
            }
            List<String> roles = userRolePermissionsInterface.getRoles(userId);
            if (roles != null) {
                authorizationInfo.addRoles(roles);
            }
        }
        return authorizationInfo;
    }

}
