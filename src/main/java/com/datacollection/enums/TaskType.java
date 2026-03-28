package com.datacollection.enums;

/**
 * 任务类型枚举
 */
public enum TaskType {
    WEBSITE("网站采集"),
    THINKTANK("智库采集"),
    SOCIAL("社交平台");
    
    private final String description;
    
    TaskType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
