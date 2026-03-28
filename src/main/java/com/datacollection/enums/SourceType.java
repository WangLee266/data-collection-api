package com.datacollection.enums;

/**
 * 信源类型枚举
 */
public enum SourceType {
    MEDIA("媒体网站"),
    GOVERNMENT("政府网站"),
    THINKTANK("智库网站"),
    ENTERPRISE("企业网站");
    
    private final String description;
    
    SourceType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
