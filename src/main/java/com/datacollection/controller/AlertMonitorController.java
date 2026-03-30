package com.datacollection.controller;

import com.datacollection.dto.*;
import com.datacollection.service.AlertMonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 预警监测任务控制器
 */
@RestController
@RequestMapping("/api/alert-monitor")
@RequiredArgsConstructor
@Tag(name = "预警监测管理", description = "预警监测任务的创建、管理和执行接口")
public class AlertMonitorController {
    
    private final AlertMonitorService alertMonitorService;
    
    @Operation(summary = "创建预警监测任务", description = "创建新的预警监测任务")
    @PostMapping("/tasks")
    public ResponseEntity<ApiResponse<AlertMonitorTaskResponse>> createTask(
            @RequestBody AlertMonitorTaskRequest request) {
        AlertMonitorTaskResponse response = alertMonitorService.createTask(request);
        return ResponseEntity.ok(ApiResponse.success(response, "创建成功"));
    }
    
    @Operation(summary = "更新预警监测任务", description = "更新预警监测任务配置")
    @PutMapping("/tasks/{id}")
    public ResponseEntity<ApiResponse<AlertMonitorTaskResponse>> updateTask(
            @Parameter(description = "任务ID") @PathVariable Long id,
            @RequestBody AlertMonitorTaskRequest request) {
        AlertMonitorTaskResponse response = alertMonitorService.updateTask(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "更新成功"));
    }
    
    @Operation(summary = "删除预警监测任务", description = "删除指定的预警监测任务")
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @Parameter(description = "任务ID") @PathVariable Long id) {
        alertMonitorService.deleteTask(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }
    
    @Operation(summary = "获取预警监测任务详情", description = "根据ID获取预警监测任务详细信息")
    @GetMapping("/tasks/{id}")
    public ResponseEntity<ApiResponse<AlertMonitorTaskResponse>> getTask(
            @Parameter(description = "任务ID") @PathVariable Long id) {
        AlertMonitorTaskResponse response = alertMonitorService.getTask(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @Operation(summary = "分页查询预警监测任务", description = "分页查询预警监测任务列表")
    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<Page<AlertMonitorTaskResponse>>> listTasks(
            @Parameter(description = "状态筛选") @RequestParam(required = false) String status,
            @Parameter(description = "目标类型筛选") @RequestParam(required = false) String targetType,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        Page<AlertMonitorTaskResponse> response = alertMonitorService.listTasks(status, targetType, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @Operation(summary = "启动预警监测任务", description = "启动指定的预警监测任务")
    @PostMapping("/tasks/{id}/start")
    public ResponseEntity<ApiResponse<AlertMonitorTaskResponse>> startTask(
            @Parameter(description = "任务ID") @PathVariable Long id) {
        AlertMonitorTaskResponse response = alertMonitorService.startTask(id);
        return ResponseEntity.ok(ApiResponse.success(response, "任务已启动"));
    }
    
    @Operation(summary = "暂停预警监测任务", description = "暂停指定的预警监测任务")
    @PostMapping("/tasks/{id}/pause")
    public ResponseEntity<ApiResponse<AlertMonitorTaskResponse>> pauseTask(
            @Parameter(description = "任务ID") @PathVariable Long id) {
        AlertMonitorTaskResponse response = alertMonitorService.pauseTask(id);
        return ResponseEntity.ok(ApiResponse.success(response, "任务已暂停"));
    }
    
    @Operation(summary = "获取预警排名数据", description = "获取目标预警排名数据")
    @GetMapping("/ranking")
    public ResponseEntity<ApiResponse<AlertRankingResponse>> getRanking(
            @Parameter(description = "时间周期：7d, 30d, 90d") @RequestParam(defaultValue = "7d") String period,
            @Parameter(description = "目标类型：website, social") @RequestParam(defaultValue = "website") String targetType) {
        AlertRankingResponse response = alertMonitorService.getRanking(period, targetType);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
