package com.datacollection.repository;

import com.datacollection.entity.AlertMonitorTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 预警监测任务Repository
 */
@Repository
public interface AlertMonitorTaskRepository extends JpaRepository<AlertMonitorTask, Long> {
    
    /**
     * 按状态查询
     */
    List<AlertMonitorTask> findByStatus(String status);
    
    /**
     * 按状态分页查询
     */
    Page<AlertMonitorTask> findByStatus(String status, Pageable pageable);
    
    /**
     * 按目标类型查询
     */
    List<AlertMonitorTask> findByTargetType(String targetType);
    
    /**
     * 按状态统计
     */
    long countByStatus(String status);
    
    /**
     * 查询运行中的任务
     */
    @Query("SELECT a FROM AlertMonitorTask a WHERE a.status = 'running'")
    List<AlertMonitorTask> findRunningTasks();
    
    /**
     * 按任务名称模糊查询
     */
    @Query("SELECT a FROM AlertMonitorTask a WHERE a.taskName LIKE %:keyword%")
    List<AlertMonitorTask> searchByTaskName(@Param("keyword") String keyword);
}
