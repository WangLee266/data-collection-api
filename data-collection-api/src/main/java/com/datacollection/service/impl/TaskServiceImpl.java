package com.datacollection.service.impl;

import com.datacollection.dto.*;
import com.datacollection.entity.Task;
import com.datacollection.entity.TaskLog;
import com.datacollection.enums.TaskStatus;
import com.datacollection.repository.TaskLogRepository;
import com.datacollection.repository.TaskRepository;
import com.datacollection.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    
    private final TaskRepository taskRepository;
    private final TaskLogRepository taskLogRepository;
    
    @Override
    @Transactional
    public Task createTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setTaskId("TASK-" + LocalDate.now().getYear() + "-" + String.format("%04d", System.currentTimeMillis() % 10000));
        task.setName(request.getName());
        task.setType(request.getType());
        task.setStrategy(request.getStrategy());
        task.setCronExpression(request.getCronExpression());
        task.setPriority(com.datacollection.enums.TaskPriority.valueOf(request.getPriority()));
        task.setCollectImages(request.getCollectImages());
        task.setCollectVideos(request.getCollectVideos());
        task.setCollectComments(request.getCollectComments());
        task.setStatus(TaskStatus.PENDING);
        task.setRemark(request.getRemark());
        
        if ("immediate".equals(request.getStartTime())) {
            task.setStartTime(LocalDateTime.now());
            task.setStatus(TaskStatus.RUNNING);
        } else {
            task.setStartTime(LocalDateTime.parse(request.getScheduledTime()));
        }
        
        return taskRepository.save(task);
    }
    
    @Override
    @Transactional
    public Task updateTask(Long id, Task task) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("任务不存在"));
        task.setId(id);
        return taskRepository.save(task);
    }
    
    @Override
    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
    
    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("任务不存在"));
    }
    
    @Override
    public Task getTaskByTaskId(String taskId) {
        return taskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new RuntimeException("任务不存在"));
    }
    
    @Override
    public List<Task> searchTasks(String keyword, String status, String platform, String type) {
        TaskStatus taskStatus = status != null ? TaskStatus.valueOf(status.toUpperCase()) : null;
        return taskRepository.searchTasks(keyword, taskStatus, platform, type);
    }
    
    @Override
    @Transactional
    public void startTask(String taskId) {
        Task task = getTaskByTaskId(taskId);
        if (task.getStatus() == TaskStatus.RUNNING) {
            throw new RuntimeException("任务已在运行中");
        }
        task.setStatus(TaskStatus.RUNNING);
        task.setProgress(0);
        taskRepository.save(task);
        
        // 记录日志
        addLog(taskId, "INFO", "任务已启动");
    }
    
    @Override
    @Transactional
    public void stopTask(String taskId) {
        Task task = getTaskByTaskId(taskId);
        task.setStatus(TaskStatus.SUCCESS);
        task.setProgress(100);
        taskRepository.save(task);
        
        addLog(taskId, "INFO", "任务已停止");
    }
    
    @Override
    @Transactional
    public void pauseTask(String taskId) {
        Task task = getTaskByTaskId(taskId);
        task.setStatus(TaskStatus.PENDING);
        taskRepository.save(task);
        
        addLog(taskId, "INFO", "任务已暂停");
    }
    
    @Override
    @Transactional
    public void resumeTask(String taskId) {
        Task task = getTaskByTaskId(taskId);
        task.setStatus(TaskStatus.RUNNING);
        taskRepository.save(task);
        
        addLog(taskId, "INFO", "任务已恢复");
    }
    
    @Override
    public TaskStatsResponse getTaskStats() {
        TaskStatsResponse stats = new TaskStatsResponse();
        stats.setRunningCount(taskRepository.countByStatus(TaskStatus.RUNNING));
        stats.setPendingCount(taskRepository.countByStatus(TaskStatus.PENDING));
        stats.setTodaySuccess(taskRepository.countCreatedToday(LocalDate.now().atStartOfDay()));
        stats.setTodayFailed(taskRepository.countByStatus(TaskStatus.ERROR));
        Long total = stats.getTodaySuccess() + stats.getTodayFailed();
        stats.setTodayTotal(total);
        if (total > 0) {
            stats.setFailRate(String.format("%.2f%%", (double) stats.getTodayFailed() / total * 100));
        } else {
            stats.setFailRate("0%");
        }
        return stats;
    }
    
    @Override
    public List<Task> getRunningTasks() {
        return taskRepository.findByStatus(TaskStatus.RUNNING);
    }
    
    @Override
    public List<?> getTaskLogs(String taskId) {
        return taskLogRepository.findByTaskIdOrderByLogTimeDesc(taskId);
    }
    
    private void addLog(String taskId, String level, String message) {
        TaskLog log = new TaskLog();
        log.setTaskId(taskId);
        log.setLogLevel(level);
        log.setMessage(message);
        taskLogRepository.save(log);
    }
}
