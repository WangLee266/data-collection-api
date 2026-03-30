package com.datacollection.master.service;

import com.datacollection.master.dto.TaskExecutionMessage;
import com.datacollection.master.entity.CollectTask;
import com.datacollection.master.entity.TaskExecutionLog;
import com.datacollection.master.kafka.TaskMessageProducer;
import com.datacollection.master.repository.CollectTaskRepository;
import com.datacollection.master.repository.TaskExecutionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 任务调度服务
 * 负责检查待执行任务并发送到Kafka
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskSchedulerService {
    
    private final CollectTaskRepository collectTaskRepository;
    private final TaskExecutionLogRepository executionLogRepository;
    private final TaskMessageProducer taskMessageProducer;
    
    /**
     * 检查并调度待执行任务
     * 
     * @return 调度的任务数量
     */
    @Transactional
    public int checkAndScheduleTasks() {
        LocalDateTime now = LocalDateTime.now();
        
        // 查询待执行的任务
        List<CollectTask> pendingTasks = collectTaskRepository.findPendingTasks(now);
        
        if (pendingTasks.isEmpty()) {
            log.debug("[Master] 没有待执行的任务");
            return 0;
        }
        
        log.info("[Master] 发现 {} 个待执行任务", pendingTasks.size());
        
        int scheduledCount = 0;
        List<String> failedTasks = new ArrayList<>();
        
        for (CollectTask task : pendingTasks) {
            try {
                // 创建执行消息
                TaskExecutionMessage message = createExecutionMessage(task);
                
                // 发送到Kafka
                taskMessageProducer.sendTaskMessage(message);
                
                // 记录执行日志
                createExecutionLog(task, message, "SENT", "任务已发送到任务队列");
                
                // 更新任务状态
                task.setStatus("RUNNING");
                task.setLastRunTime(now);
                collectTaskRepository.save(task);
                
                scheduledCount++;
                
            } catch (Exception e) {
                log.error("[Master] 调度任务失败: taskId={}, error={}", 
                        task.getTaskId(), e.getMessage(), e);
                failedTasks.add(task.getTaskId());
                
                // 记录失败日志
                createExecutionLog(task, null, "FAILED", "调度失败: " + e.getMessage());
            }
        }
        
        if (!failedTasks.isEmpty()) {
            log.warn("[Master] 部分任务调度失败: {}", failedTasks);
        }
        
        return scheduledCount;
    }
    
    /**
     * 创建任务执行消息
     */
    private TaskExecutionMessage createExecutionMessage(CollectTask task) {
        TaskExecutionMessage message = new TaskExecutionMessage();
        message.setExecutionId(generateExecutionId());
        message.setTaskId(task.getTaskId());
        message.setTaskName(task.getName());
        message.setTaskType(task.getType());
        message.setSourceId(task.getSourceId());
        message.setSourceName(task.getSourceName());
        message.setPlatform(task.getPlatform());
        message.setPriority(task.getPriority());
        message.setScheduleTime(LocalDateTime.now());
        message.setTimeout(300); // 默认超时5分钟
        message.setRetryCount(0);
        
        // 设置采集配置
        TaskExecutionMessage.CollectConfig config = new TaskExecutionMessage.CollectConfig();
        config.setCollectImages(task.getCollectImages() != null ? task.getCollectImages() : true);
        config.setCollectVideos(task.getCollectVideos() != null ? task.getCollectVideos() : false);
        config.setCollectComments(task.getCollectComments() != null ? task.getCollectComments() : false);
        config.setMaxCount(100); // 默认最大采集数量
        message.setCollectConfig(config);
        
        return message;
    }
    
    /**
     * 生成执行ID
     */
    private String generateExecutionId() {
        return "EXEC-" + System.currentTimeMillis() + "-" + 
               UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * 创建执行日志
     */
    private void createExecutionLog(CollectTask task, TaskExecutionMessage message, 
                                   String status, String messageText) {
        TaskExecutionLog log = new TaskExecutionLog();
        log.setTaskId(task.getTaskId());
        log.setExecutionId(message != null ? message.getExecutionId() : null);
        log.setStatus(status);
        log.setMessage(messageText);
        log.setExecuteTime(LocalDateTime.now());
        executionLogRepository.save(log);
    }
    
    /**
     * 获取调度统计信息
     */
    public SchedulerStats getStats() {
        SchedulerStats stats = new SchedulerStats();
        stats.setPendingTasks(collectTaskRepository.findByStatus("PENDING").size());
        stats.setRunningTasks(collectTaskRepository.findByStatus("RUNNING").size());
        stats.setTotalScheduledToday(executionLogRepository.findByStatus("SENT").size());
        return stats;
    }
    
    @lombok.Data
    public static class SchedulerStats {
        private int pendingTasks;
        private int runningTasks;
        private int totalScheduledToday;
    }
}
