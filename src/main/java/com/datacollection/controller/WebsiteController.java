package com.datacollection.controller;

import com.datacollection.dto.*;
import com.datacollection.entity.Website;
import com.datacollection.service.WebsiteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 网站信源控制器
 */
@Tag(name = "网站信源管理", description = "网站信源的增删改查、批量导入等接口")
@RestController
@RequestMapping("/sources/websites")
@RequiredArgsConstructor
public class WebsiteController {
    
    private final WebsiteService websiteService;
    
    @Operation(summary = "添加网站")
    @PostMapping
    public ApiResponse<Website> addWebsite(@RequestBody Website website) {
        return ApiResponse.success("网站添加成功", websiteService.addWebsite(website));
    }
    
    @Operation(summary = "批量导入网站")
    @PostMapping("/batch")
    public ApiResponse<Void> batchImport(@RequestBody List<Website> websites) {
        websiteService.batchImport(websites);
        return ApiResponse.success("批量导入成功");
    }
    
    @Operation(summary = "更新网站")
    @PutMapping("/{id}")
    public ApiResponse<Website> updateWebsite(@PathVariable Long id, @RequestBody Website website) {
        return ApiResponse.success("网站更新成功", websiteService.updateWebsite(id, website));
    }
    
    @Operation(summary = "删除网站")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteWebsite(@PathVariable Long id) {
        websiteService.deleteWebsite(id);
        return ApiResponse.success("网站删除成功");
    }
    
    @Operation(summary = "获取网站详情")
    @GetMapping("/{id}")
    public ApiResponse<Website> getWebsite(@PathVariable Long id) {
        return ApiResponse.success(websiteService.getWebsiteById(id));
    }
    
    @Operation(summary = "搜索网站")
    @GetMapping("/search")
    public ApiResponse<List<Website>> searchWebsites(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String domain,
            @RequestParam(required = false) String sourceType) {
        return ApiResponse.success(websiteService.searchWebsites(keyword, language, domain, sourceType));
    }
    
    @Operation(summary = "获取网站数量")
    @GetMapping("/count")
    public ApiResponse<Long> countWebsites() {
        return ApiResponse.success(websiteService.countActive());
    }
}
