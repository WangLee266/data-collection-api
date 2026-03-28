package com.datacollection.enums;

/**
 * 节点状态枚举
 */
public enum NodeStatus {
    NORMAL("正常"),
    WARNING("警告"),
    ERROR("离线/异常");
    
    private final String description;
    
    NodeStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
