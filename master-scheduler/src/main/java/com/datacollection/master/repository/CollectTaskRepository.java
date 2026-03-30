package com.datacollection.master.repository;

import com.datacollection.master.entity.CollectTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 采集任务Repository
 */
@Repository
public interface CollectTaskRepository extends JpaRepository<CollectTask, Long> {
    
    /**
     * 查询待执行的任务（下次执行时间小于当前时间，且状态为PENDING）
     */
    @Query("SELECT t FROM CollectTask t WHERE t.nextRunTime <= :now AND t.status = 'PENDING' ORDER BY t.priority DESC, t.nextRunTime ASC")
    List<CollectTask> findPendingTasks(@Param("now") LocalDateTime now);
    
    /**
     * 查询运行中的任务
     */
    List<CollectTask> findByStatus(String status);
    
    /**
     * 按任务ID查询
     */
    CollectTask findByTaskId(String taskId);
    
    /**
     * 查询指定时间范围内的待执行任务
     */
    @Query("SELECT t FROM CollectTask t WHERE t.nextRunTime BETWEEN :start AND :end AND t.status IN ('PENDING', 'RUNNING')")
    List<CollectTask> findTasksInTimeRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
