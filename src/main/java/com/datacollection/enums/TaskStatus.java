package com.datacollection.enums;

/**
 * 任务状态枚举
 */
public enum TaskStatus {
    RUNNING("运行中"),
    PENDING("等待中"),
    SUCCESS("成功"),
    ERROR("失败"),
    WARNING("警告");
    
    private final String description;
    
    TaskStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
