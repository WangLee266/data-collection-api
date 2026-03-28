package com.datacollection.dto;

import lombok.Data;

/**
 * 节点统计响应
 */
@Data
public class NodeStatsResponse {
    
    private Integer totalNodes;
    private Integer onlineNodes;
    private Integer offlineNodes;
    private Integer errorNodes;
    private Double avgCpu;
    private Double avgMemory;
    private Integer runningTasks;
    private Integer pendingTasks;
    private Integer warningNodes;
}
