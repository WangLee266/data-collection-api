package com.datacollection.service;

import com.datacollection.entity.Organization;
import java.util.List;

/**
 * 组织服务接口
 */
public interface OrganizationService {
    
    /**
     * 添加组织
     */
    Organization addOrganization(Organization organization);
    
    /**
     * 更新组织
     */
    Organization updateOrganization(Long id, Organization organization);
    
    /**
     * 删除组织
     */
    void deleteOrganization(Long id);
    
    /**
     * 获取组织详情
     */
    Organization getOrganizationById(Long id);
    
    /**
     * 搜索组织
     */
    List<Organization> searchOrganizations(String keyword, String country, String type);
    
    /**
     * 获取活跃组织数量
     */
    Long countActive();
}
