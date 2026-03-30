package com.datacollection.service;

import com.datacollection.dto.*;

/**
 * 概览统计服务接口
 */
public interface OverviewService {
    
    /**
     * 获取概览统计数据
     */
    OverviewStatsResponse getOverviewStats();
    
    /**
     * 获取信源统计
     */
    SourceStatsResponse getSourceStats();
}
