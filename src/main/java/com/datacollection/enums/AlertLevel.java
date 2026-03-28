package com.datacollection.enums;

/**
 * 预警级别枚举
 */
public enum AlertLevel {
    CRITICAL("紧急"),
    SERIOUS("严重"),
    NORMAL("一般");
    
    private final String description;
    
    AlertLevel(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
