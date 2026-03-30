package com.datacollection.enums;

/**
 * 任务优先级枚举
 */
public enum TaskPriority {
    HIGH("高"),
    NORMAL("普通"),
    LOW("低");
    
    private final String description;
    
    TaskPriority(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
