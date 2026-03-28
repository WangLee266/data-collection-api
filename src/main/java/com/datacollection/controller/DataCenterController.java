package com.datacollection.controller;

import com.datacollection.dto.*;
import com.datacollection.entity.CollectData;
import com.datacollection.service.DataCenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 数据中心控制器
 */
@Tag(name = "数据中心", description = "采集数据的查询、搜索、导出等接口")
@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataCenterController {
    
    private final DataCenterService dataCenterService;
    
    @Operation(summary = "搜索数据")
    @GetMapping("/search")
    public ApiResponse<List<CollectData>> searchData(
            @RequestParam(required = false) String mainTab,
            @RequestParam(required = false) String sourceType,
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String category) {
        return ApiResponse.success(dataCenterService.searchData(mainTab, sourceType, platform, keyword, country, language, category));
    }
    
    @Operation(summary = "获取数据详情")
    @GetMapping("/{id}")
    public ApiResponse<CollectData> getData(@PathVariable Long id) {
        return ApiResponse.success(dataCenterService.getDataById(id));
    }
    
    @Operation(summary = "批量导出数据")
    @PostMapping("/export")
    public ResponseEntity<byte[]> exportData(@RequestBody List<Long> ids, @RequestParam(defaultValue = "csv") String format) {
        byte[] data = dataCenterService.exportData(ids, format);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data_export.csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }
    
    @Operation(summary = "获取数据总量")
    @GetMapping("/count")
    public ApiResponse<Long> countData() {
        return ApiResponse.success(dataCenterService.countTotal());
    }
    
    @Operation(summary = "获取今日采集量")
    @GetMapping("/count/today")
    public ApiResponse<Long> countToday() {
        return ApiResponse.success(dataCenterService.countToday());
    }
}
