package com.datacollection.repository;

import com.datacollection.entity.Task;
import com.datacollection.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    Optional<Task> findByTaskId(String taskId);
    
    List<Task> findByStatus(TaskStatus status);
    
    List<Task> findByNodeId(String nodeId);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = :status")
    Long countByStatus(TaskStatus status);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.createTime >= :startTime")
    Long countCreatedToday(LocalDateTime startTime);
    
    @Query("SELECT SUM(t.collectedCount) FROM Task t")
    Long sumCollectedCount();
    
    @Query("SELECT t FROM Task t WHERE " +
           "(:keyword IS NULL OR t.name LIKE %:keyword% OR t.taskId LIKE %:keyword% OR t.sourceName LIKE %:keyword%) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:platform IS NULL OR t.platform = :platform) AND " +
           "(:type IS NULL OR t.type = :type)")
    List<Task> searchTasks(String keyword, TaskStatus status, String platform, String type);
    
    @Query("SELECT t.type, COUNT(t) FROM Task t GROUP BY t.type")
    List<Object[]> countByType();
}
