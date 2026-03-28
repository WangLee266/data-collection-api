package com.datacollection.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 任务日志实体
 */
@Data
@Entity
@Table(name = "collect_task_log")
public class TaskLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "task_id", nullable = false, length = 30)
    private String taskId;
    
    @Column(name = "node_id", length = 30)
    private String nodeId;
    
    @Column(name = "log_type", length = 20)
    private String logType;
    
    @Column(name = "log_level", length = 20)
    private String logLevel;
    
    @Column(columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "log_time")
    private LocalDateTime logTime;
    
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        logTime = LocalDateTime.now();
    }
}
