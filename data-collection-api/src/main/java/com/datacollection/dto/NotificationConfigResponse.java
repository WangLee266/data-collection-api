package com.datacollection.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 通知配置响应DTO
 */
@Data
public class NotificationConfigResponse {
    
    private Long id;
    
    private String configType;
    
    private Boolean isEnabled;
    
    private String alertLevel;
    
    private String configName;
    
    private String configValue;
    
    private String extraConfig;
    
    private Integer sortOrder;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}
