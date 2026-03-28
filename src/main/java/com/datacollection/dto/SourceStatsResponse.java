package com.datacollection.dto;

import lombok.Data;

/**
 * 信源统计响应
 */
@Data
public class SourceStatsResponse {
    
    private Long personCount;
    private Long orgCount;
    private Long websiteCount;
    private Long socialCount;
    private Long totalSources;
    private Long validSources;
}
