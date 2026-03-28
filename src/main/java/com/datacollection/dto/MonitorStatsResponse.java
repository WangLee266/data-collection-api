package com.datacollection.dto;

import lombok.Data;

/**
 * 数据监控统计响应
 */
@Data
public class MonitorStatsResponse {
    
    private Long monitorCount;
    private Long focusCount;
    private Long pendingAlerts;
    private Long criticalCount;
    private Long seriousCount;
    private Long processingCount;
    private Long todayResolved;
    private Long todayTotal;
    private String todayTrend;
}
