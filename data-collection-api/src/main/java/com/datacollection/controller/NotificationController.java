package com.datacollection.controller;

import com.datacollection.dto.*;
import com.datacollection.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知配置控制器
 */
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@Tag(name = "通知配置管理", description = "系统通知、邮件、企业微信、短信通知配置接口")
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @Operation(summary = "创建通知配置", description = "创建新的通知配置")
    @PostMapping("/configs")
    public ResponseEntity<ApiResponse<NotificationConfigResponse>> createConfig(
            @RequestBody NotificationConfigRequest request) {
        NotificationConfigResponse response = notificationService.createConfig(request);
        return ResponseEntity.ok(ApiResponse.success(response, "创建成功"));
    }
    
    @Operation(summary = "更新通知配置", description = "更新通知配置信息")
    @PutMapping("/configs/{id}")
    public ResponseEntity<ApiResponse<NotificationConfigResponse>> updateConfig(
            @Parameter(description = "配置ID") @PathVariable Long id,
            @RequestBody NotificationConfigRequest request) {
        NotificationConfigResponse response = notificationService.updateConfig(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "更新成功"));
    }
    
    @Operation(summary = "删除通知配置", description = "删除指定的通知配置")
    @DeleteMapping("/configs/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteConfig(
            @Parameter(description = "配置ID") @PathVariable Long id) {
        notificationService.deleteConfig(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }
    
    @Operation(summary = "获取通知配置详情", description = "根据ID获取通知配置详细信息")
    @GetMapping("/configs/{id}")
    public ResponseEntity<ApiResponse<NotificationConfigResponse>> getConfig(
            @Parameter(description = "配置ID") @PathVariable Long id) {
        NotificationConfigResponse response = notificationService.getConfig(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @Operation(summary = "查询通知配置列表", description = "查询通知配置列表，支持按类型筛选")
    @GetMapping("/configs")
    public ResponseEntity<ApiResponse<List<NotificationConfigResponse>>> listConfigs(
            @Parameter(description = "配置类型：sys, email, wechat, sms") @RequestParam(required = false) String configType) {
        List<NotificationConfigResponse> response = notificationService.listByType(configType);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @Operation(summary = "获取启用的通知配置", description = "获取所有已启用的通知配置")
    @GetMapping("/configs/enabled")
    public ResponseEntity<ApiResponse<List<NotificationConfigResponse>>> listEnabledConfigs() {
        List<NotificationConfigResponse> response = notificationService.listEnabled();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @Operation(summary = "启用/禁用通知配置", description = "切换通知配置的启用状态")
    @PostMapping("/configs/{id}/toggle")
    public ResponseEntity<ApiResponse<NotificationConfigResponse>> toggleConfig(
            @Parameter(description = "配置ID") @PathVariable Long id,
            @Parameter(description = "是否启用") @RequestParam Boolean enabled) {
        NotificationConfigResponse response = notificationService.toggleConfig(id, enabled);
        return ResponseEntity.ok(ApiResponse.success(response, enabled ? "已启用" : "已禁用"));
    }
}
