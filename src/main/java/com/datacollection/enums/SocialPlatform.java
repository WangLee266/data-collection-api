package com.datacollection.enums;

/**
 * 社交平台枚举
 */
public enum SocialPlatform {
    X("X (Twitter)"),
    FACEBOOK("Facebook"),
    INSTAGRAM("Instagram"),
    YOUTUBE("YouTube"),
    TIKTOK("TikTok"),
    LINKEDIN("LinkedIn");
    
    private final String description;
    
    SocialPlatform(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
