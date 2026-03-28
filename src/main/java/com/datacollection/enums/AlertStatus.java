package com.datacollection.enums;

/**
 * 预警状态枚举
 */
public enum AlertStatus {
    PENDING("未处理"),
    PROCESSING("处理中"),
    RESOLVED("已恢复");
    
    private final String description;
    
    AlertStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
