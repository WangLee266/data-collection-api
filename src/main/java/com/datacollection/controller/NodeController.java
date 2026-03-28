package com.datacollection.controller;

import com.datacollection.dto.*;
import com.datacollection.entity.Node;
import com.datacollection.service.NodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 节点管理控制器
 */
@Tag(name = "节点管理", description = "采集节点的添加、删除、状态监控等接口")
@RestController
@RequestMapping("/nodes")
@RequiredArgsConstructor
public class NodeController {
    
    private final NodeService nodeService;
    
    @Operation(summary = "添加节点")
    @PostMapping
    public ApiResponse<Node> addNode(@RequestBody Node node) {
        return ApiResponse.success("节点添加成功", nodeService.addNode(node));
    }
    
    @Operation(summary = "更新节点")
    @PutMapping("/{id}")
    public ApiResponse<Node> updateNode(@PathVariable Long id, @RequestBody Node node) {
        return ApiResponse.success("节点更新成功", nodeService.updateNode(id, node));
    }
    
    @Operation(summary = "删除节点")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNode(@PathVariable Long id) {
        nodeService.deleteNode(id);
        return ApiResponse.success("节点删除成功");
    }
    
    @Operation(summary = "获取节点详情")
    @GetMapping("/{id}")
    public ApiResponse<Node> getNode(@PathVariable Long id) {
        return ApiResponse.success(nodeService.getNodeById(id));
    }
    
    @Operation(summary = "获取所有节点")
    @GetMapping
    public ApiResponse<List<Node>> getAllNodes() {
        return ApiResponse.success(nodeService.getAllNodes());
    }
    
    @Operation(summary = "获取在线节点")
    @GetMapping("/online")
    public ApiResponse<List<Node>> getOnlineNodes() {
        return ApiResponse.success(nodeService.getOnlineNodes());
    }
    
    @Operation(summary = "重启节点")
    @PostMapping("/{nodeId}/restart")
    public ApiResponse<Void> restartNode(@PathVariable String nodeId) {
        nodeService.restartNode(nodeId);
        return ApiResponse.success("节点已重启");
    }
    
    @Operation(summary = "关闭节点")
    @PostMapping("/{nodeId}/shutdown")
    public ApiResponse<Void> shutdownNode(@PathVariable String nodeId) {
        nodeService.shutdownNode(nodeId);
        return ApiResponse.success("节点已关闭");
    }
    
    @Operation(summary = "更新节点心跳")
    @PostMapping("/{nodeId}/heartbeat")
    public ApiResponse<Void> updateHeartbeat(@PathVariable String nodeId) {
        nodeService.updateHeartbeat(nodeId);
        return ApiResponse.success("心跳更新成功");
    }
    
    @Operation(summary = "获取节点统计")
    @GetMapping("/stats")
    public ApiResponse<NodeStatsResponse> getNodeStats() {
        return ApiResponse.success(nodeService.getNodeStats());
    }
}
