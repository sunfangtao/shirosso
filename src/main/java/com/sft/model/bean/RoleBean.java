package com.sft.model.bean;

import com.sft.model.Role;

public class RoleBean extends Role {
    private String create_name;// 创建者名称

    public String getCreate_name() {
        return create_name;
    }

    public void setCreate_name(String create_name) {
        this.create_name = create_name;
    }
}
