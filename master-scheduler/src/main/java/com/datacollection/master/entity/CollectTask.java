package com.datacollection.master.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 采集任务实体（只读，用于查询待执行任务）
 */
@Data
@Entity
@Table(name = "collect_task")
public class CollectTask {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "task_id", unique = true, length = 30)
    private String taskId;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, length = 20)
    private String type;
    
    @Column(name = "source_id")
    private Long sourceId;
    
    @Column(name = "source_name", length = 100)
    private String sourceName;
    
    @Column(length = 30)
    private String platform;
    
    @Column(length = 50)
    private String strategy;
    
    @Column(name = "cron_expression", length = 50)
    private String cronExpression;
    
    @Column(length = 20)
    private String status;
    
    @Column
    private Integer progress;
    
    @Column(name = "collected_count")
    private Long collectedCount;
    
    @Column(name = "total_count")
    private Long totalCount;
    
    @Column(name = "node_id", length = 30)
    private String nodeId;
    
    @Column(name = "next_run_time")
    private LocalDateTime nextRunTime;
    
    @Column(name = "last_run_time")
    private LocalDateTime lastRunTime;
    
    @Column(length = 20)
    private String priority;
    
    @Column(name = "collect_images")
    private Boolean collectImages;
    
    @Column(name = "collect_videos")
    private Boolean collectVideos;
    
    @Column(name = "collect_comments")
    private Boolean collectComments;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(length = 50)
    private String creator;
    
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    
    @Column(columnDefinition = "TEXT")
    private String remark;
}
