package com.sft.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class SSOLogoutFilter extends LogoutFilter {

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        // 在这里执行退出系统前需要清空的数据
        Subject subject = getSubject(request, response);
//        Cache<Object, Object> cache = cacheManager.getCache("authenticationCache");
//        cache.remove(subject.getPrincipal());
        return super.preHandle(request, response);
    }
}
