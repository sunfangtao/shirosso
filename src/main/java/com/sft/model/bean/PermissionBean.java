package com.sft.model.bean;

import com.sft.model.Permission;

public class PermissionBean extends Permission {
    private String module;// 模块名称
    private boolean isHas;// 是否拥有

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public boolean isHas() {
        return isHas;
    }

    public void setHas(boolean has) {
        isHas = has;
    }
}
