package com.sft.filter.service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface NoPermissionService {

    public boolean onHandleAccessDenied(ServletRequest request, ServletResponse response);
}
