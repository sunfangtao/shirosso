package com.sft.realm;

import com.sft.service.UserRolePermissionsInterface;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.List;

public class ShiroSSORealm extends CasRealm {

    private static Logger logger = Logger.getLogger(ShiroSSORealm.class);

    private UserRolePermissionsInterface userRolePermissionsInterface;

    public void setUserRolePermissionsInterface(UserRolePermissionsInterface userRolePermissionsInterface) {
        this.userRolePermissionsInterface = userRolePermissionsInterface;
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
