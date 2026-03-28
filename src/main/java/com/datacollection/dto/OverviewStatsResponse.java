package com.datacollection.dto;

import lombok.Data;
import java.util.List;

/**
 * 统计概览响应
 */
@Data
public class OverviewStatsResponse {
    
    private StatCards statCards;
    private List<TrendData> trendData;
    private List<PlatformData> platformData;
    private List<RegionData> regionData;
    private List<SourceCategoryData> sourceCategoryData;
    
    @Data
    public static class StatCards {
        private Long totalSources;
        private Long validSources;
        private String totalData;
        private Long todayCollected;
        private String todayTrend;
        private Integer onlineNodes;
        private Integer totalNodes;
        private Integer runningTasks;
        private Long todayExecutions;
    }
    
    @Data
    public static class TrendData {
        private String day;
        private Integer website;
        private Integer thinkTank;
        private Integer x;
        private Integer facebook;
        private Integer instagram;
        private Integer youtube;
        private Integer tiktok;
    }
    
    @Data
    public static class PlatformData {
        private String name;
        private Long count;
        private String color;
    }
    
    @Data
    public static class RegionData {
        private String id;
        private String label;
        private Long count;
    }
    
    @Data
    public static class SourceCategoryData {
        private String label;
        private Long total;
        private Long valid;
        private String color;
    }
}
