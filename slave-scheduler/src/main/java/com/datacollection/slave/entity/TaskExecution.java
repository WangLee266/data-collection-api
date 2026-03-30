package com.datacollection.slave.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 任务执行记录实体
 */
@Data
@Entity
@Table(name = "slave_task_execution")
public class TaskExecution {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "execution_id", unique = true, length = 50)
    private String executionId;
    
    @Column(name = "task_id", nullable = false, length = 30)
    private String taskId;
    
    @Column(name = "node_id", length = 30)
    private String nodeId;
    
    @Column(name = "kafka_partition")
    private Integer kafkaPartition;
    
    @Column(name = "kafka_offset")
    private Long kafkaOffset;
    
    @Column(length = 20)
    private String status;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "data_count")
    private Integer dataCount;
    
    @Column(columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "retry_count")
    private Integer retryCount;
    
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (retryCount == null) {
            retryCount = 0;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
