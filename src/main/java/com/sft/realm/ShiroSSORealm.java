/**
 * @Project:crm
 * @Title:PasswordShiroRealm.java
 * @Author:Riozenc
 * @Datetime:2016年10月16日 下午8:04:07
 */
package com.sft.realm;

import com.sft.model.Role;
import com.sft.model.UserModel;
import com.sft.model.bean.RoleBean;
import com.sft.service.PermissionService;
import com.sft.service.RoleService;
import com.sft.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShiroSSORealm extends CasRealm {

    @Resource
    private PermissionService permissionService;
    @Resource
    private RoleService roleService;
    @Resource
    private UserService userService;

    protected final Map<String, SimpleAuthorizationInfo> authorizationMap = new ConcurrentHashMap<String, SimpleAuthorizationInfo>();

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String account = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = null;
        if (authorizationInfo == null) {
            authorizationInfo = new SimpleAuthorizationInfo();
            List<String> permissions = permissionService.getPermissions(account);
            if (permissions != null) {
                authorizationInfo.addStringPermissions(permissions);
            }
            List<RoleBean> roles = roleService.getRoles(account);
            if (roles != null) {
                List<String> roleNameList = new ArrayList<String>();
                for (Role role : roles) {
                    roleNameList.add(role.getName());
                }
                authorizationInfo.addRoles(roleNameList);
            }
            authorizationMap.put(account, authorizationInfo);
        }
        return authorizationInfo;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        AuthenticationInfo authc = super.doGetAuthenticationInfo(authenticationToken);

        String account = (String) authc.getPrincipals().getPrimaryPrincipal();
        UserModel user = userService.getUserByAccount(account);
        SecurityUtils.getSubject().getSession().setAttribute("userId", user.getId());

        return authc;
    }
}
