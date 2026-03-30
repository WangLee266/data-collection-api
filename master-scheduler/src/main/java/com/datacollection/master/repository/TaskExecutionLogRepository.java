package com.datacollection.master.repository;

import com.datacollection.master.entity.TaskExecutionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务执行日志Repository
 */
@Repository
public interface TaskExecutionLogRepository extends JpaRepository<TaskExecutionLog, Long> {
    
    /**
     * 按任务ID查询执行日志
     */
    List<TaskExecutionLog> findByTaskIdOrderByCreateTimeDesc(String taskId);
    
    /**
     * 按执行ID查询
     */
    TaskExecutionLog findByExecutionId(String executionId);
    
    /**
     * 按状态查询
     */
    List<TaskExecutionLog> findByStatus(String status);
}
