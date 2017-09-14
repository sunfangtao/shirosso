/**
 * Title:PasswordShiroFilter.java
 * Author:czy
 * Datetime:2016年10月24日 下午3:21:01
 */
package com.sft.filter;

import com.sft.token.UsernamePasswordToken;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class PasswordShiroFilter extends FormAuthenticationFilter {

    private Logger logger = Logger.getLogger(PasswordShiroFilter.class);

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        if (password == null) {
            password = "";
        }
        String isPhoneLogin = request.getParameter("isPhoneLogin");
        logger.info("createToken");
        return new UsernamePasswordToken(username, password.toCharArray(), "1".equals(isPhoneLogin) ? true : false);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                     ServletResponse response) throws Exception {
        return super.onLoginSuccess(token, subject, request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
                                     ServletResponse response) {
        return super.onLoginFailure(token, e, request, response);
    }

}
