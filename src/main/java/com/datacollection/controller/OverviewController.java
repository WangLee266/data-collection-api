package com.datacollection.controller;

import com.datacollection.dto.*;
import com.datacollection.service.OverviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 概览统计控制器
 */
@Tag(name = "概览统计", description = "系统首页统计数据、信源统计等接口")
@RestController
@RequestMapping("/overview")
@RequiredArgsConstructor
public class OverviewController {
    
    private final OverviewService overviewService;
    
    @Operation(summary = "获取概览统计数据")
    @GetMapping("/stats")
    public ApiResponse<OverviewStatsResponse> getOverviewStats() {
        return ApiResponse.success(overviewService.getOverviewStats());
    }
    
    @Operation(summary = "获取信源统计")
    @GetMapping("/sources/stats")
    public ApiResponse<SourceStatsResponse> getSourceStats() {
        return ApiResponse.success(overviewService.getSourceStats());
    }
}
