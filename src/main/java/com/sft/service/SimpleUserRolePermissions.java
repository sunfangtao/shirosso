package com.sft.service;

import com.sft.util.SendJSONUtil;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;

import java.util.List;

public abstract class SimpleUserRolePermissions implements UserRolePermissionsInterface {

    public List<String> getAnonUrl() {
        return null;
    }

    public String doResolveException(Exception ex) {
        String resultJson = null;
        if (ex instanceof UnauthorizedException) {
            // 没有授权
            resultJson = SendJSONUtil.getFailResultObject("UnauthorizedException", "您无权访问！");
        } else if (ex instanceof UnauthenticatedException) {
            // 没有认证
            resultJson = SendJSONUtil.getFailResultObject("UnauthenticatedException", "您还没有登录！");
        }
        return resultJson;
    }
}
