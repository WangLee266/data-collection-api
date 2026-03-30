package com.datacollection.master.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 任务执行记录实体
 */
@Data
@Entity
@Table(name = "task_execution_log")
public class TaskExecutionLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "task_id", nullable = false, length = 30)
    private String taskId;
    
    @Column(name = "execution_id", length = 50)
    private String executionId;
    
    @Column(name = "node_id", length = 30)
    private String nodeId;
    
    @Column(name = "kafka_partition")
    private Integer kafkaPartition;
    
    @Column(name = "kafka_offset")
    private Long kafkaOffset;
    
    @Column(name = "status", length = 20)
    private String status;
    
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "execute_time")
    private LocalDateTime executeTime;
    
    @Column(name = "complete_time")
    private LocalDateTime completeTime;
    
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (executeTime == null) {
            executeTime = LocalDateTime.now();
        }
    }
}
