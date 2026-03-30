package com.datacollection.slave.controller;

import com.datacollection.slave.dto.CollectDataMessage;
import com.datacollection.slave.entity.CollectNode;
import com.datacollection.slave.entity.TaskExecution;
import com.datacollection.slave.kafka.DataMessageProducer;
import com.datacollection.slave.node.NodeSelector;
import com.datacollection.slave.repository.CollectNodeRepository;
import com.datacollection.slave.repository.TaskExecutionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 分调度中心控制器
 * 提供数据接收接口和节点管理接口
 */
@Slf4j
@RestController
@RequestMapping("/api/slave")
@RequiredArgsConstructor
@Tag(name = "分调度中心管理", description = "分调度中心管理接口")
public class SlaveSchedulerController {
    
    private final DataMessageProducer dataMessageProducer;
    private final NodeSelector nodeSelector;
    private final CollectNodeRepository nodeRepository;
    private final TaskExecutionRepository executionRepository;
    
    /**
     * 数据接收接口
     * 供Python数据推送服务调用，将采集数据推送到Kafka
     */
    @Operation(summary = "接收采集数据", description = "接收采集节点推送的采集数据")
    @PostMapping("/data/receive")
    public ResponseEntity<Map<String, Object>> receiveData(
            @RequestBody CollectDataMessage dataMessage) {
        
        log.info("[Slave] 收到数据推送: dataId={}, taskId={}, title={}", 
                dataMessage.getDataId(), 
                dataMessage.getTaskId(),
                truncate(dataMessage.getTitle(), 50));
        
        try {
            // 设置数据ID和时间
            if (dataMessage.getDataId() == null || dataMessage.getDataId().isEmpty()) {
                dataMessage.setDataId(UUID.randomUUID().toString());
            }
            if (dataMessage.getCollectTime() == null) {
                dataMessage.setCollectTime(LocalDateTime.now());
            }
            
            // 发送到Kafka
            dataMessageProducer.sendDataMessage(dataMessage);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "数据已接收并发送",
                "dataId", dataMessage.getDataId()
            ));
            
        } catch (Exception e) {
            log.error("[Slave] 处理数据失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "处理失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 批量数据接收接口
     */
    @Operation(summary = "批量接收采集数据", description = "批量接收采集节点推送的采集数据")
    @PostMapping("/data/receive/batch")
    public ResponseEntity<Map<String, Object>> receiveDataBatch(
            @RequestBody List<CollectDataMessage> dataList) {
        
        log.info("[Slave] 收到批量数据推送: count={}", dataList.size());
        
        try {
            int successCount = 0;
            for (CollectDataMessage data : dataList) {
                if (data.getDataId() == null || data.getDataId().isEmpty()) {
                    data.setDataId(UUID.randomUUID().toString());
                }
                if (data.getCollectTime() == null) {
                    data.setCollectTime(LocalDateTime.now());
                }
                dataMessageProducer.sendDataMessage(data);
                successCount++;
            }
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "批量数据已接收",
                "total", dataList.size(),
                "successCount", successCount
            ));
            
        } catch (Exception e) {
            log.error("[Slave] 批量处理数据失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "处理失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 获取分调度中心状态
     */
    @Operation(summary = "获取调度状态", description = "获取分调度中心运行状态")
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "slave-scheduler");
        status.put("status", "running");
        status.put("timestamp", System.currentTimeMillis());
        status.put("onlineNodes", nodeSelector.countOnlineNodes());
        status.put("runningTasks", executionRepository.findByStatus("RUNNING").size());
        
        return ResponseEntity.ok(status);
    }
    
    /**
     * 获取在线节点列表
     */
    @Operation(summary = "获取在线节点", description = "获取所有在线节点列表")
    @GetMapping("/nodes")
    public ResponseEntity<List<CollectNode>> getOnlineNodes() {
        List<CollectNode> nodes = nodeSelector.getOnlineNodes();
        return ResponseEntity.ok(nodes);
    }
    
    /**
     * 获取节点详情
     */
    @Operation(summary = "获取节点详情", description = "获取指定节点详细信息")
    @GetMapping("/nodes/{nodeId}")
    public ResponseEntity<?> getNode(@PathVariable String nodeId) {
        return nodeSelector.getNode(nodeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 注册节点
     */
    @Operation(summary = "注册节点", description = "注册新的采集节点")
    @PostMapping("/nodes/register")
    public ResponseEntity<Map<String, Object>> registerNode(@RequestBody CollectNode node) {
        log.info("[Slave] 注册节点: nodeId={}, ip={}", node.getNodeId(), node.getIp());
        
        node.setStatus("NORMAL");
        node.setTaskCount(0);
        node.setLastHeartbeat(LocalDateTime.now());
        node.setCreateTime(LocalDateTime.now());
        node.setUpdateTime(LocalDateTime.now());
        
        nodeRepository.save(node);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "节点注册成功",
            "nodeId", node.getNodeId()
        ));
    }
    
    /**
     * 节点心跳
     */
    @Operation(summary = "节点心跳", description = "更新节点心跳状态")
    @PostMapping("/nodes/{nodeId}/heartbeat")
    public ResponseEntity<Map<String, Object>> heartbeat(@PathVariable String nodeId) {
        return nodeSelector.getNode(nodeId)
                .map(node -> {
                    node.setLastHeartbeat(LocalDateTime.now());
                    node.setUpdateTime(LocalDateTime.now());
                    nodeRepository.save(node);
                    
                    return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "心跳更新成功"
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 获取任务执行记录
     */
    @Operation(summary = "获取执行记录", description = "获取任务执行记录")
    @GetMapping("/executions")
    public ResponseEntity<List<TaskExecution>> getExecutions() {
        List<TaskExecution> executions = executionRepository.findAll();
        return ResponseEntity.ok(executions);
    }
    
    private String truncate(String str, int maxLength) {
        if (str == null) return null;
        return str.length() > maxLength ? str.substring(0, maxLength) + "..." : str;
    }
}
