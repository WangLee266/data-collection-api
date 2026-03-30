package com.datacollection.controller;

import com.datacollection.dto.*;
import com.datacollection.entity.Alert;
import com.datacollection.service.MonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 数据监控控制器
 */
@Tag(name = "数据监控", description = "预警管理、监控统计等接口")
@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class MonitorController {
    
    private final MonitorService monitorService;
    
    @Operation(summary = "获取预警列表")
    @GetMapping("/alerts")
    public ApiResponse<List<Alert>> getAlerts(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String level) {
        return ApiResponse.success(monitorService.getAlerts(status, level));
    }
    
    @Operation(summary = "获取预警详情")
    @GetMapping("/alerts/{id}")
    public ApiResponse<Alert> getAlert(@PathVariable Long id) {
        return ApiResponse.success(monitorService.getAlertById(id));
    }
    
    @Operation(summary = "处理预警")
    @PostMapping("/alerts/{id}/process")
    public ApiResponse<Void> processAlert(@PathVariable Long id, @RequestParam String action) {
        monitorService.processAlert(id, action);
        return ApiResponse.success("预警已处理");
    }
    
    @Operation(summary = "解决预警")
    @PostMapping("/alerts/{id}/resolve")
    public ApiResponse<Void> resolveAlert(@PathVariable Long id, @RequestParam String resolveUser) {
        monitorService.resolveAlert(id, resolveUser);
        return ApiResponse.success("预警已解决");
    }
    
    @Operation(summary = "创建预警")
    @PostMapping("/alerts")
    public ApiResponse<Alert> createAlert(@RequestBody Alert alert) {
        return ApiResponse.success("预警创建成功", monitorService.createAlert(alert));
    }
    
    @Operation(summary = "获取监控统计")
    @GetMapping("/stats")
    public ApiResponse<MonitorStatsResponse> getMonitorStats() {
        return ApiResponse.success(monitorService.getMonitorStats());
    }
}
