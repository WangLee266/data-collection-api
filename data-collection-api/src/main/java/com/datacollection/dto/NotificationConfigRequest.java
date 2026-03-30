package com.datacollection.dto;

import lombok.Data;
import java.util.List;

/**
 * 通知配置请求DTO
 */
@Data
public class NotificationConfigRequest {
    
    private String configType;
    
    private Boolean isEnabled;
    
    private String alertLevel;
    
    private String configName;
    
    private String configValue;
    
    private String extraConfig;
    
    private Integer sortOrder;
}
