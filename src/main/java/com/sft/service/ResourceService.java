package com.sft.service;

import com.sft.bean.ResourcePermission;

import java.util.List;

public interface ResourceService {

    public List<ResourcePermission> getAllResourcePer();

    public boolean addResourcePer(ResourcePermission resourcePermission);

    public boolean updateResourcePer(ResourcePermission resourcePermission);
} 