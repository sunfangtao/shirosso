package com.sft.service;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ShiroSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {

    private UserRolePermissionsInterface userRolePermissionsInterface;

    public void setUserRolePermissionsInterface(UserRolePermissionsInterface userRolePermissionsInterface) {
        this.userRolePermissionsInterface = userRolePermissionsInterface;
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            String resultJson = userRolePermissionsInterface.doResolveException(ex);
            if (resultJson != null) {
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(resultJson);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.doResolveException(request, response, handler, ex);
    }
}
