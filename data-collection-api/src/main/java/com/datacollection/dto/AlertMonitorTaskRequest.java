package com.datacollection.dto;

import lombok.Data;
import java.util.List;

/**
 * 预警监测任务请求DTO
 */
@Data
public class AlertMonitorTaskRequest {
    
    private String taskName;
    
    private String targetType;
    
    private List<String> targetIds;
    
    private String frequency;
    
    private String monitorTimeStart;
    
    private String monitorTimeEnd;
    
    private Integer noDataThreshold;
    
    private String noDataUnit;
    
    private Integer successRateThreshold;
    
    private Integer dataChangeThreshold;
    
    private Boolean structureChangeDetect;
    
    private Boolean rateLimitDetect;
    
    private List<String> notifyChannels;
}
