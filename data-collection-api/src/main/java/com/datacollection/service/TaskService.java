package com.datacollection.service;

import com.datacollection.dto.*;
import com.datacollection.entity.Task;
import java.util.List;

/**
 * 任务服务接口
 */
public interface TaskService {
    
    /**
     * 创建任务
     */
    Task createTask(CreateTaskRequest request);
    
    /**
     * 更新任务
     */
    Task updateTask(Long id, Task task);
    
    /**
     * 删除任务
     */
    void deleteTask(Long id);
    
    /**
     * 获取任务详情
     */
    Task getTaskById(Long id);
    
    /**
     * 获取任务详情（按任务ID）
     */
    Task getTaskByTaskId(String taskId);
    
    /**
     * 搜索任务
     */
    List<Task> searchTasks(String keyword, String status, String platform, String type);
    
    /**
     * 启动任务
     */
    void startTask(String taskId);
    
    /**
     * 停止任务
     */
    void stopTask(String taskId);
    
    /**
     * 暂停任务
     */
    void pauseTask(String taskId);
    
    /**
     * 恢复任务
     */
    void resumeTask(String taskId);
    
    /**
     * 获取任务统计
     */
    TaskStatsResponse getTaskStats();
    
    /**
     * 获取所有运行中的任务
     */
    List<Task> getRunningTasks();
    
    /**
     * 获取任务日志
     */
    List<?> getTaskLogs(String taskId);
}
