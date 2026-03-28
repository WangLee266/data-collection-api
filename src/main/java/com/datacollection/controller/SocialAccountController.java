package com.datacollection.controller;

import com.datacollection.entity.SocialAccount;
import com.datacollection.service.SocialAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 社交账号控制器
 */
@Tag(name = "社交账号管理", description = "社交账号的增删改查、批量导入等接口")
@RestController
@RequestMapping("/sources/social")
@RequiredArgsConstructor
public class SocialAccountController {
    
    private final SocialAccountService socialAccountService;
    
    @Operation(summary = "添加账号")
    @PostMapping
    public ApiResponse<SocialAccount> addAccount(@RequestBody SocialAccount account) {
        return ApiResponse.success("账号添加成功", socialAccountService.addAccount(account));
    }
    
    @Operation(summary = "批量导入账号")
    @PostMapping("/batch")
    public ApiResponse<Void> batchImport(@RequestBody List<SocialAccount> accounts) {
        socialAccountService.batchImport(accounts);
        return ApiResponse.success("批量导入成功");
    }
    
    @Operation(summary = "更新账号")
    @PutMapping("/{id}")
    public ApiResponse<SocialAccount> updateAccount(@PathVariable Long id, @RequestBody SocialAccount account) {
        return ApiResponse.success("账号更新成功", socialAccountService.updateAccount(id, account));
    }
    
    @Operation(summary = "删除账号")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAccount(@PathVariable Long id) {
        socialAccountService.deleteAccount(id);
        return ApiResponse.success("账号删除成功");
    }
    
    @Operation(summary = "获取账号详情")
    @GetMapping("/{id}")
    public ApiResponse<SocialAccount> getAccount(@PathVariable Long id) {
        return ApiResponse.success(socialAccountService.getAccountById(id));
    }
    
    @Operation(summary = "搜索账号")
    @GetMapping("/search")
    public ApiResponse<List<SocialAccount>> searchAccounts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String domain) {
        return ApiResponse.success(socialAccountService.searchAccounts(keyword, platform, domain));
    }
    
    @Operation(summary = "按平台获取账号")
    @GetMapping("/platform/{platform}")
    public ApiResponse<List<SocialAccount>> getByPlatform(@PathVariable String platform) {
        return ApiResponse.success(socialAccountService.getAccountsByPlatform(platform));
    }
    
    @Operation(summary = "获取账号数量")
    @GetMapping("/count")
    public ApiResponse<Long> countAccounts() {
        return ApiResponse.success(socialAccountService.countActive());
    }
}
