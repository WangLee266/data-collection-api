package com.datacollection.service;

import com.datacollection.dto.*;
import com.datacollection.entity.CollectData;
import java.util.List;

/**
 * 数据中心服务接口
 */
public interface DataCenterService {
    
    /**
     * 高级搜索数据
     */
    List<CollectData> searchData(String mainTab, String sourceType, String platform, 
                                   String keyword, String country, String language, String category);
    
    /**
     * 获取数据详情
     */
    CollectData getDataById(Long id);
    
    /**
     * 批量导出数据
     */
    byte[] exportData(List<Long> ids, String format);
    
    /**
     * 获取数据总量
     */
    Long countTotal();
    
    /**
     * 获取今日采集量
     */
    Long countToday();
}
