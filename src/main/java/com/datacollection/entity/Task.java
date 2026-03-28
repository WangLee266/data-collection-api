package com.datacollection.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 采集任务实体
 */
@Data
@Entity
@Table(name = "collect_task")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "task_id", unique = true, nullable = false, length = 30)
    private String taskId;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "type", nullable = false, length = 20)
    private String type;
    
    @Column(name = "source_id")
    private Long sourceId;
    
    @Column(name = "source_name", length = 100)
    private String sourceName;
    
    @Column(name = "platform", length = 30)
    private String platform;
    
    @Column(name = "strategy", length = 50)
    private String strategy;
    
    @Column(name = "cron_expression", length = 50)
    private String cronExpression;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private com.datacollection.enums.TaskStatus status = com.datacollection.enums.TaskStatus.PENDING;
    
    @Column(name = "progress")
    private Integer progress = 0;
    
    @Column(name = "collected_count")
    private Long collectedCount = 0L;
    
    @Column(name = "total_count")
    private Long totalCount;
    
    @Column(name = "node_id", length = 30)
    private String nodeId;
    
    @Column(name = "next_run_time")
    private LocalDateTime nextRunTime;
    
    @Column(name = "last_run_time")
    private LocalDateTime lastRunTime;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", length = 20)
    private com.datacollection.enums.TaskPriority priority = com.datacollection.enums.TaskPriority.NORMAL;
    
    @Column(name = "collect_images")
    private Boolean collectImages = true;
    
    @Column(name = "collect_videos")
    private Boolean collectVideos = false;
    
    @Column(name = "collect_comments")
    private Boolean collectComments = false;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "creator", length = 50)
    private String creator;
    
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    
    @Column(columnDefinition = "TEXT")
    private String remark;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (taskId == null) {
            taskId = "TASK-" + java.time.LocalDate.now().getYear() + "-" + String.format("%04d", id);
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
