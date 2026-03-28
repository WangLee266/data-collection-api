package com.datacollection.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 预警实体
 */
@Data
@Entity
@Table(name = "monitor_alert")
public class Alert {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "alert_id", unique = true, nullable = false, length = 30)
    private String alertId;
    
    @Column(name = "source_id")
    private Long sourceId;
    
    @Column(length = 100)
    private String source;
    
    @Column(length = 30)
    private String platform;
    
    @Column(name = "alert_type", length = 50)
    private String alertType;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private com.datacollection.enums.AlertLevel level;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private com.datacollection.enums.AlertStatus status = com.datacollection.enums.AlertStatus.PENDING;
    
    @Column(columnDefinition = "TEXT")
    private String detail;
    
    @Column(name = "alert_time")
    private LocalDateTime alertTime;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
    
    @Column(name = "resolve_time")
    private LocalDateTime resolveTime;
    
    @Column(name = "resolve_user", length = 50)
    private String resolveUser;
    
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        alertTime = LocalDateTime.now();
        if (alertId == null) {
            alertId = "ALT-" + String.format("%03d", id);
        }
    }
}
