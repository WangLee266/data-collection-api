package com.datacollection.dto;

import lombok.Data;
import java.util.List;

/**
 * 预警排名项DTO
 */
@Data
public class AlertRankingResponse {
    
    private String period;
    
    private String targetType;
    
    private List<RankItem> items;
    
    @Data
    public static class RankItem {
        private Integer rank;
        private String name;
        private Integer count;
        private String type;
        private String platform;
    }
}
