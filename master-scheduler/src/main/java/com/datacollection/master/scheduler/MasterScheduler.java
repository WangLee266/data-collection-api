package com.datacollection.master.scheduler;

import com.datacollection.master.service.TaskSchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 主调度定时任务
 * 定时检查数据库任务表，将待执行任务推送到Kafka
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MasterScheduler {
    
    private final TaskSchedulerService taskSchedulerService;
    
    @Value("${scheduler.enabled:true}")
    private boolean schedulerEnabled;
    
    /**
     * 定时检查待执行任务
     * 每30秒执行一次
     */
    @Scheduled(fixedRate = 30000)
    public void schedulePendingTasks() {
        if (!schedulerEnabled) {
            return;
        }
        
        try {
            log.debug("[Master] 开始检查待执行任务...");
            int scheduledCount = taskSchedulerService.checkAndScheduleTasks();
            
            if (scheduledCount > 0) {
                log.info("[Master] 本次调度完成，共调度 {} 个任务", scheduledCount);
            }
            
        } catch (Exception e) {
            log.error("[Master] 调度检查异常: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 定时清理过期任务
     * 每小时执行一次
     */
    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredTasks() {
        if (!schedulerEnabled) {
            return;
        }
        
        try {
            log.info("[Master] 开始清理过期任务...");
            // TODO: 实现过期任务清理逻辑
            log.info("[Master] 过期任务清理完成");
            
        } catch (Exception e) {
            log.error("[Master] 清理过期任务异常: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 定时输出调度状态
     * 每5分钟执行一次
     */
    @Scheduled(fixedRate = 300000)
    public void logSchedulerStatus() {
        if (!schedulerEnabled) {
            return;
        }
        
        try {
            TaskSchedulerService.SchedulerStats stats = taskSchedulerService.getStats();
            log.info("[Master] 调度状态 - 待执行: {}, 运行中: {}, 今日已调度: {}", 
                    stats.getPendingTasks(), 
                    stats.getRunningTasks(), 
                    stats.getTotalScheduledToday());
            
        } catch (Exception e) {
            log.error("[Master] 获取调度状态异常: {}", e.getMessage(), e);
        }
    }
}
