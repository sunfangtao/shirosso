package com.sft.dao;

import com.sft.model.Module;
import com.sft.model.bean.ModuleBean;

import java.util.List;

public interface ServiceDao {

    public List<ModuleBean> getAllModule();
}
