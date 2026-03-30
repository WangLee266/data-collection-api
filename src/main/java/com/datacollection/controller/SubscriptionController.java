package com.datacollection.controller;

import com.datacollection.dto.*;
import com.datacollection.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 订阅账号管理控制器
 */
@RestController
@RequestMapping("/api/subscription")
@RequiredArgsConstructor
@Tag(name = "订阅账号管理", description = "网站订阅账号和社交平台订阅账号管理接口")
public class SubscriptionController {
    
    private final SubscriptionService subscriptionService;
    
    // ============= 网站订阅账号接口 =============
    
    @Operation(summary = "创建网站订阅账号", description = "录入新的网站订阅账号信息")
    @PostMapping("/website")
    public ResponseEntity<ApiResponse<WebsiteSubscriptionResponse>> createWebsiteAccount(
            @RequestBody WebsiteSubscriptionRequest request) {
        WebsiteSubscriptionResponse response = subscriptionService.createWebsiteAccount(request);
        return ResponseEntity.ok(ApiResponse.success(response, "创建成功"));
    }
    
    @Operation(summary = "更新网站订阅账号", description = "更新网站订阅账号信息")
    @PutMapping("/website/{id}")
    public ResponseEntity<ApiResponse<WebsiteSubscriptionResponse>> updateWebsiteAccount(
            @Parameter(description = "账号ID") @PathVariable Long id,
            @RequestBody WebsiteSubscriptionRequest request) {
        WebsiteSubscriptionResponse response = subscriptionService.updateWebsiteAccount(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "更新成功"));
    }
    
    @Operation(summary = "删除网站订阅账号", description = "删除指定的网站订阅账号")
    @DeleteMapping("/website/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWebsiteAccount(
            @Parameter(description = "账号ID") @PathVariable Long id) {
        subscriptionService.deleteWebsiteAccount(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }
    
    @Operation(summary = "获取网站订阅账号详情", description = "根据ID获取网站订阅账号详细信息")
    @GetMapping("/website/{id}")
    public ResponseEntity<ApiResponse<WebsiteSubscriptionResponse>> getWebsiteAccount(
            @Parameter(description = "账号ID") @PathVariable Long id) {
        WebsiteSubscriptionResponse response = subscriptionService.getWebsiteAccount(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @Operation(summary = "分页查询网站订阅账号", description = "分页查询网站订阅账号列表，支持按网站名称、状态、账号名称筛选")
    @GetMapping("/website")
    public ResponseEntity<ApiResponse<Page<WebsiteSubscriptionResponse>>> listWebsiteAccounts(
            @Parameter(description = "网站名称关键词") @RequestParam(required = false) String websiteName,
            @Parameter(description = "状态筛选") @RequestParam(required = false) String status,
            @Parameter(description = "账号名称关键词") @RequestParam(required = false) String accountName,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        Page<WebsiteSubscriptionResponse> response = subscriptionService.listWebsiteAccounts(
            websiteName, status, accountName, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    // ============= 社交平台订阅账号接口 =============
    
    @Operation(summary = "创建社交平台订阅账号", description = "录入新的社交平台订阅账号信息")
    @PostMapping("/social")
    public ResponseEntity<ApiResponse<SocialSubscriptionResponse>> createSocialAccount(
            @RequestBody SocialSubscriptionRequest request) {
        SocialSubscriptionResponse response = subscriptionService.createSocialAccount(request);
        return ResponseEntity.ok(ApiResponse.success(response, "创建成功"));
    }
    
    @Operation(summary = "更新社交平台订阅账号", description = "更新社交平台订阅账号信息")
    @PutMapping("/social/{id}")
    public ResponseEntity<ApiResponse<SocialSubscriptionResponse>> updateSocialAccount(
            @Parameter(description = "账号ID") @PathVariable Long id,
            @RequestBody SocialSubscriptionRequest request) {
        SocialSubscriptionResponse response = subscriptionService.updateSocialAccount(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "更新成功"));
    }
    
    @Operation(summary = "删除社交平台订阅账号", description = "删除指定的社交平台订阅账号")
    @DeleteMapping("/social/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSocialAccount(
            @Parameter(description = "账号ID") @PathVariable Long id) {
        subscriptionService.deleteSocialAccount(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }
    
    @Operation(summary = "获取社交平台订阅账号详情", description = "根据ID获取社交平台订阅账号详细信息")
    @GetMapping("/social/{id}")
    public ResponseEntity<ApiResponse<SocialSubscriptionResponse>> getSocialAccount(
            @Parameter(description = "账号ID") @PathVariable Long id) {
        SocialSubscriptionResponse response = subscriptionService.getSocialAccount(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @Operation(summary = "分页查询社交平台订阅账号", description = "分页查询社交平台订阅账号列表，支持按平台、状态、账号名称筛选")
    @GetMapping("/social")
    public ResponseEntity<ApiResponse<Page<SocialSubscriptionResponse>>> listSocialAccounts(
            @Parameter(description = "平台筛选") @RequestParam(required = false) String platform,
            @Parameter(description = "状态筛选") @RequestParam(required = false) String status,
            @Parameter(description = "账号名称关键词") @RequestParam(required = false) String accountName,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        Page<SocialSubscriptionResponse> response = subscriptionService.listSocialAccounts(
            platform, status, accountName, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    // ============= 统计接口 =============
    
    @Operation(summary = "获取订阅账号统计信息", description = "获取网站和社交平台订阅账号的统计数据")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<SubscriptionStatsResponse>> getStats() {
        SubscriptionStatsResponse response = subscriptionService.getStats();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
