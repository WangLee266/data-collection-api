package com.datacollection.repository;

import com.datacollection.entity.Alert;
import com.datacollection.enums.AlertLevel;
import com.datacollection.enums.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    
    Optional<Alert> findByAlertId(String alertId);
    
    List<Alert> findByStatus(AlertStatus status);
    
    List<Alert> findByLevel(AlertLevel level);
    
    @Query("SELECT COUNT(a) FROM Alert a WHERE a.status = 'PENDING'")
    Long countPending();
    
    @Query("SELECT COUNT(a) FROM Alert a WHERE a.status = 'PROCESSING'")
    Long countProcessing();
    
    @Query("SELECT COUNT(a) FROM Alert a WHERE a.alertTime >= :startTime")
    Long countToday(LocalDateTime startTime);
    
    @Query("SELECT COUNT(a) FROM Alert a WHERE a.status = 'RESOLVED' AND a.resolveTime >= :startTime")
    Long countTodayResolved(LocalDateTime startTime);
    
    @Query("SELECT a FROM Alert a WHERE " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:level IS NULL OR a.level = :level) AND " +
           "a.alertTime >= :startTime ORDER BY a.alertTime DESC")
    List<Alert> searchAlerts(AlertStatus status, AlertLevel level, LocalDateTime startTime);
}
