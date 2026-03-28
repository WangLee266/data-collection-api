package com.datacollection.dto;

import lombok.Data;

/**
 * 任务统计响应
 */
@Data
public class TaskStatsResponse {
    
    private Long runningCount;
    private Long pendingCount;
    private Long todaySuccess;
    private Long todayFailed;
    private Long todayTotal;
    private String failRate;
    private Double successTrend;
}
