package com.datacollection.master.controller;

import com.datacollection.master.dto.TaskExecutionMessage;
import com.datacollection.master.entity.CollectTask;
import com.datacollection.master.kafka.TaskMessageProducer;
import com.datacollection.master.repository.CollectTaskRepository;
import com.datacollection.master.service.TaskSchedulerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主调度中心管理接口
 */
@Slf4j
@RestController
@RequestMapping("/api/master")
@RequiredArgsConstructor
@Tag(name = "主调度中心管理", description = "主调度中心管理接口")
public class MasterSchedulerController {
    
    private final TaskSchedulerService taskSchedulerService;
    private final TaskMessageProducer taskMessageProducer;
    private final CollectTaskRepository collectTaskRepository;
    
    @Operation(summary = "获取调度状态", description = "获取主调度中心运行状态")
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "master-scheduler");
        status.put("status", "running");
        status.put("timestamp", System.currentTimeMillis());
        
        TaskSchedulerService.SchedulerStats stats = taskSchedulerService.getStats();
        status.put("pendingTasks", stats.getPendingTasks());
        status.put("runningTasks", stats.getRunningTasks());
        status.put("totalScheduledToday", stats.getTotalScheduledToday());
        
        return ResponseEntity.ok(status);
    }
    
    @Operation(summary = "手动触发调度", description = "手动触发一次任务调度检查")
    @PostMapping("/trigger")
    public ResponseEntity<Map<String, Object>> triggerSchedule() {
        int scheduledCount = taskSchedulerService.checkAndScheduleTasks();
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("scheduledCount", scheduledCount);
        result.put("message", "成功调度 " + scheduledCount + " 个任务");
        
        return ResponseEntity.ok(result);
    }
    
    @Operation(summary = "获取待执行任务列表", description = "获取当前待执行的任务列表")
    @GetMapping("/tasks/pending")
    public ResponseEntity<List<CollectTask>> getPendingTasks() {
        List<CollectTask> tasks = collectTaskRepository.findByStatus("PENDING");
        return ResponseEntity.ok(tasks);
    }
    
    @Operation(summary = "获取运行中任务列表", description = "获取当前运行中的任务列表")
    @GetMapping("/tasks/running")
    public ResponseEntity<List<CollectTask>> getRunningTasks() {
        List<CollectTask> tasks = collectTaskRepository.findByStatus("RUNNING");
        return ResponseEntity.ok(tasks);
    }
    
    @Operation(summary = "发送测试消息", description = "发送测试任务消息到Kafka")
    @PostMapping("/test/send")
    public ResponseEntity<Map<String, Object>> sendTestMessage(
            @RequestParam String taskId) {
        
        CollectTask task = collectTaskRepository.findByTaskId(taskId);
        if (task == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "任务不存在: " + taskId
            ));
        }
        
        TaskExecutionMessage message = new TaskExecutionMessage();
        message.setExecutionId("TEST-" + System.currentTimeMillis());
        message.setTaskId(task.getTaskId());
        message.setTaskName(task.getName());
        message.setTaskType(task.getType());
        message.setScheduleTime(java.time.LocalDateTime.now());
        
        taskMessageProducer.sendTaskMessage(message);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "测试消息已发送",
            "executionId", message.getExecutionId()
        ));
    }
}
