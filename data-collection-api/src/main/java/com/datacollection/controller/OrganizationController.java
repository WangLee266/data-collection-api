package com.datacollection.controller;

import com.datacollection.entity.Organization;
import com.datacollection.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 组织信源控制器
 */
@Tag(name = "组织管理", description = "组织信源的增删改查接口")
@RestController
@RequestMapping("/sources/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    
    private final OrganizationService organizationService;
    
    @Operation(summary = "添加组织")
    @PostMapping
    public ApiResponse<Organization> addOrganization(@RequestBody Organization organization) {
        return ApiResponse.success("组织添加成功", organizationService.addOrganization(organization));
    }
    
    @Operation(summary = "更新组织")
    @PutMapping("/{id}")
    public ApiResponse<Organization> updateOrganization(@PathVariable Long id, @RequestBody Organization organization) {
        return ApiResponse.success("组织更新成功", organizationService.updateOrganization(id, organization));
    }
    
    @Operation(summary = "删除组织")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ApiResponse.success("组织删除成功");
    }
    
    @Operation(summary = "获取组织详情")
    @GetMapping("/{id}")
    public ApiResponse<Organization> getOrganization(@PathVariable Long id) {
        return ApiResponse.success(organizationService.getOrganizationById(id));
    }
    
    @Operation(summary = "搜索组织")
    @GetMapping("/search")
    public ApiResponse<List<Organization>> searchOrganizations(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String type) {
        return ApiResponse.success(organizationService.searchOrganizations(keyword, country, type));
    }
    
    @Operation(summary = "获取组织数量")
    @GetMapping("/count")
    public ApiResponse<Long> countOrganizations() {
        return ApiResponse.success(organizationService.countActive());
    }
}
