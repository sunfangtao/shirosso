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
import org.apache.log4j.Logger;
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

    private static Logger logger = Logger.getLogger(ShiroSSORealm.class);

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
        String userId = userService.getUserByAccount(account).getId();
        SimpleAuthorizationInfo authorizationInfo = null;
        if (authorizationInfo == null) {
            authorizationInfo = new SimpleAuthorizationInfo();
            List<String> permissions = permissionService.getPermissions(userId);
            if (permissions != null) {
                authorizationInfo.addStringPermissions(permissions);
            }
            List<RoleBean> roles = roleService.getRoles(userId);
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

//        if (authenticationToken instanceof UsernamePasswordToken) {
//            logger.info("app登录");
//            UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//            String username = token.getUsername();
//
//            if (sessionManager instanceof DefaultWebSessionManager) {
//                DefaultWebSessionManager defaultWebSessionManager = (DefaultWebSessionManager) sessionManager;
//
//                SessionDAO sessionDAO = defaultWebSessionManager.getSessionDAO();
//                Collection<Session> sessions = sessionDAO.getActiveSessions();
//
//                for (Session session : sessions) {
//                    // 清除该用户以前登录时保存的session
//                    if (token.getPrincipal().equals(
//                            String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
//                        sessionDAO.delete(session);
//                        logger.info("已在线的用户：" + token.getPrincipal() + "被踢出");
//                    }
//                }
//            }
//
//            if (!StringUtils.hasText(username)) {
//                return null;
//            }
//            UserModel user = userService.getUserById(username);
//            if (user == null) {
//                return null;
//            }
//            String password = user.getPassword();
//            if (!StringUtils.hasText(password)) {
//                return null;
//            }
//
//            setCredentialsMatcher(retryLimitHashedCredentialsMatcher);
//            return new SimpleAuthenticationInfo(username, password.substring(32), ByteSource.Util.bytes(user
//                    .getPassword().substring(0, 32)), getName());
//        } else {
            logger.info("页面登录");
            AuthenticationInfo authc = super.doGetAuthenticationInfo(authenticationToken);

            if (authc != null) {
                String account = (String) authc.getPrincipals().getPrimaryPrincipal();
                UserModel user = userService.getUserByAccount(account);
                SecurityUtils.getSubject().getSession().setAttribute("userId", user.getId());
            }
            return authc;
//        }
    }
}
