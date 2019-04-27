package com.sft.filter;

import com.sft.filter.service.NoPermissionService;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class RolePermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {

    private NoPermissionService noPermissionService;

    public void setNoPermissionService(NoPermissionService noPermissionService) {
        this.noPermissionService = noPermissionService;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = this.getSubject(request, response);
        if (subject.getPrincipal() == null) {
            this.saveRequestAndRedirectToLogin(request, response);
        } else {
            if (noPermissionService == null || !noPermissionService.onHandleAccessDenied(request, response)) {
                String unauthorizedUrl = this.getUnauthorizedUrl();
                if (StringUtils.hasText(unauthorizedUrl)) {
                    WebUtils.issueRedirect(request, response, unauthorizedUrl);
                } else {
                    WebUtils.toHttp(response).sendError(401);
                }
            }
        }
        return false;
    }

}