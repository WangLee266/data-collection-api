package com.datacollection.slave.repository;

import com.datacollection.slave.entity.TaskExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务执行Repository
 */
@Repository
public interface TaskExecutionRepository extends JpaRepository<TaskExecution, Long> {
    
    /**
     * 按执行ID查询
     */
    TaskExecution findByExecutionId(String executionId);
    
    /**
     * 按任务ID查询
     */
    List<TaskExecution> findByTaskIdOrderByCreateTimeDesc(String taskId);
    
    /**
     * 按状态查询
     */
    List<TaskExecution> findByStatus(String status);
    
    /**
     * 按节点ID查询
     */
    List<TaskExecution> findByNodeIdOrderByCreateTimeDesc(String nodeId);
}
