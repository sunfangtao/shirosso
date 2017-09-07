package com.sft.service;

import com.sft.model.bean.ModuleBean;

import java.util.List;

public interface ModuleService {

    /**
     * 获取系统所有模块
     *
     * @return
     */
    public List<ModuleBean> getAllModule();
}
