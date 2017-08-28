package com.sft.bean;

import java.io.Serializable;

public class ResourcePermission implements Serializable {
    // 主键id
    private String id;
    // action url
    private String url;
    // shiro permission;
    private String permission;

    private int del_flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(int del_flag) {
        this.del_flag = del_flag;
    }
}