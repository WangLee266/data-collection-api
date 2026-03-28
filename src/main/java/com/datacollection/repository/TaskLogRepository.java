package com.datacollection.repository;

import com.datacollection.entity.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {
    
    List<TaskLog> findByTaskId(String taskId);
    
    @Query("SELECT l FROM TaskLog l WHERE l.taskId = :taskId ORDER BY l.logTime DESC")
    List<TaskLog> findByTaskIdOrderByLogTimeDesc(String taskId);
    
    @Query("SELECT l FROM TaskLog l WHERE l.taskId = :taskId AND l.logTime >= :startTime ORDER BY l.logTime DESC")
    List<TaskLog> findRecentLogs(String taskId, LocalDateTime startTime);
}
