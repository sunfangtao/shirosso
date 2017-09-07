package com.sft.service.impl;

import com.sft.dao.ServiceDao;
import com.sft.model.bean.ModuleBean;
import com.sft.service.ModuleService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class ModuleServiceImpl implements ModuleService {

    @Resource
    private ServiceDao serviceDao;

    public List<ModuleBean> getAllModule() {
        return serviceDao.getAllModule();
    }
}
