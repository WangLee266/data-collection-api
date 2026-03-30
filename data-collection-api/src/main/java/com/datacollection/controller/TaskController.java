package com.datacollection.controller;

import com.datacollection.dto.*;
import com.datacollection.entity.Task;
import com.datacollection.entity.TaskLog;
import com.datacollection.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 任务管理控制器
 */
@Tag(name = "任务管理", description = "采集任务的创建、启动、停止、查询等接口")
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    
    private final TaskService taskService;
    
    @Operation(summary = "创建任务")
    @PostMapping
    public ApiResponse<Task> createTask(@Valid @RequestBody CreateTaskRequest request) {
        return ApiResponse.success("任务创建成功", taskService.createTask(request));
    }
    
    @Operation(summary = "更新任务")
    @PutMapping("/{id}")
    public ApiResponse<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return ApiResponse.success("任务更新成功", taskService.updateTask(id, task));
    }
    
    @Operation(summary = "删除任务")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ApiResponse.success("任务删除成功");
    }
    
    @Operation(summary = "获取任务详情")
    @GetMapping("/{id}")
    public ApiResponse<Task> getTask(@PathVariable Long id) {
        return ApiResponse.success(taskService.getTaskById(id));
    }
    
    @Operation(summary = "搜索任务")
    @GetMapping("/search")
    public ApiResponse<List<Task>> searchTasks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String type) {
        return ApiResponse.success(taskService.searchTasks(keyword, status, platform, type));
    }
    
    @Operation(summary = "启动任务")
    @PostMapping("/{taskId}/start")
    public ApiResponse<Void> startTask(@PathVariable String taskId) {
        taskService.startTask(taskId);
        return ApiResponse.success("任务已启动");
    }
    
    @Operation(summary = "停止任务")
    @PostMapping("/{taskId}/stop")
    public ApiResponse<Void> stopTask(@PathVariable String taskId) {
        taskService.stopTask(taskId);
        return ApiResponse.success("任务已停止");
    }
    
    @Operation(summary = "暂停任务")
    @PostMapping("/{taskId}/pause")
    public ApiResponse<Void> pauseTask(@PathVariable String taskId) {
        taskService.pauseTask(taskId);
        return ApiResponse.success("任务已暂停");
    }
    
    @Operation(summary = "恢复任务")
    @PostMapping("/{taskId}/resume")
    public ApiResponse<Void> resumeTask(@PathVariable String taskId) {
        taskService.resumeTask(taskId);
        return ApiResponse.success("任务已恢复");
    }
    
    @Operation(summary = "获取任务统计")
    @GetMapping("/stats")
    public ApiResponse<TaskStatsResponse> getTaskStats() {
        return ApiResponse.success(taskService.getTaskStats());
    }
    
    @Operation(summary = "获取运行中的任务")
    @GetMapping("/running")
    public ApiResponse<List<Task>> getRunningTasks() {
        return ApiResponse.success(taskService.getRunningTasks());
    }
    
    @Operation(summary = "获取任务日志")
    @GetMapping("/{taskId}/logs")
    public ApiResponse<List<TaskLog>> getTaskLogs(@PathVariable String taskId) {
        return ApiResponse.success((List<TaskLog>) taskService.getTaskLogs(taskId));
    }
}
