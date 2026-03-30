package com.datacollection.dto;

import lombok.Data;
import java.util.List;

/**
 * 订阅账号统计响应DTO
 */
@Data
public class SubscriptionStatsResponse {
    
    /**
     * 网站订阅账号总数
     */
    private Integer websiteAccountTotal;
    
    /**
     * 网站订阅有效账号数
     */
    private Integer websiteAccountValid;
    
    /**
     * 网站订阅已过期账号数
     */
    private Integer websiteAccountExpired;
    
    /**
     * 网站订阅即将到期账号数
     */
    private Integer websiteAccountExpiring;
    
    /**
     * 社交平台账号总数
     */
    private Integer socialAccountTotal;
    
    /**
     * 社交平台有效账号数
     */
    private Integer socialAccountValid;
    
    /**
     * 社交平台受限账号数
     */
    private Integer socialAccountLimited;
    
    /**
     * 社交平台封禁账号数
     */
    private Integer socialAccountBanned;
    
    /**
     * 网站账号平台分布
     */
    private List<PlatformDistribution> websiteDistribution;
    
    /**
     * 社交账号平台分布
     */
    private List<PlatformDistribution> socialDistribution;
    
    @Data
    public static class PlatformDistribution {
        private String name;
        private Integer count;
    }
}
