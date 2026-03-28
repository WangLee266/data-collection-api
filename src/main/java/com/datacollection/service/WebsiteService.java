package com.datacollection.service;

import com.datacollection.dto.*;
import com.datacollection.entity.Website;
import java.util.List;

/**
 * 网站信源服务接口
 */
public interface WebsiteService {
    
    /**
     * 添加网站
     */
    Website addWebsite(Website website);
    
    /**
     * 批量导入网站
     */
    void batchImport(List<Website> websites);
    
    /**
     * 更新网站
     */
    Website updateWebsite(Long id, Website website);
    
    /**
     * 删除网站
     */
    void deleteWebsite(Long id);
    
    /**
     * 获取网站详情
     */
    Website getWebsiteById(Long id);
    
    /**
     * 搜索网站
     */
    List<Website> searchWebsites(String keyword, String language, String domain, String sourceType);
    
    /**
     * 获取网站统计
     */
    Long countActive();
}
