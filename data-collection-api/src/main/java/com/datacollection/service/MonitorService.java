package com.datacollection.service;

import com.datacollection.dto.*;
import com.datacollection.entity.Alert;
import java.util.List;

/**
 * 数据监控服务接口
 */
public interface MonitorService {
    
    /**
     * 获取预警列表
     */
    List<Alert> getAlerts(String status, String level);
    
    /**
     * 获取预警详情
     */
    Alert getAlertById(Long id);
    
    /**
     * 处理预警
     */
    void processAlert(Long id, String action);
    
    /**
     * 解决预警
     */
    void resolveAlert(Long id, String resolveUser);
    
    /**
     * 获取监控统计
     */
    MonitorStatsResponse getMonitorStats();
    
    /**
     * 创建预警
     */
    Alert createAlert(Alert alert);
}
