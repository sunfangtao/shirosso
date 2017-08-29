package com.sft.filter;

import com.sft.util.CloudError;
import com.sft.util.SendJSONUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RolePermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = this.getSubject(request, response);
        if (subject.getPrincipal() == null) {
            this.saveRequestAndRedirectToLogin(request, response);
        } else {
            if (request instanceof HttpServletRequest && isAjaxRequest((HttpServletRequest) request)) {
                String returnJson = SendJSONUtil.getFailResultObject(CloudError.ReasonEnum.PERMISSION.getValue(), "您没有足够的权限执行该操作!");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                response.getWriter().write(returnJson);
            } else {
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

    private boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        if (requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }
}