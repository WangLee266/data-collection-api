package com.datacollection.master.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 采集数据实体（用于存储采集的数据）
 */
@Data
@Entity
@Table(name = "collect_data")
public class CollectData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "data_id", unique = true, length = 50)
    private String dataId;
    
    @Column(name = "task_id", length = 30)
    private String taskId;
    
    @Column(name = "node_id", length = 30)
    private String nodeId;
    
    @Column(name = "main_tab", length = 20)
    private String mainTab;
    
    @Column(length = 500)
    private String title;
    
    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;
    
    @Column(length = 255)
    private String source;
    
    @Column(name = "source_type", length = 20)
    private String sourceType;
    
    @Column(length = 30)
    private String platform;
    
    @Column(length = 50)
    private String country;
    
    @Column(length = 30)
    private String language;
    
    @Column(name = "content_type", length = 20)
    private String contentType;
    
    @Column(name = "publish_time")
    private LocalDateTime publishTime;
    
    @Column(name = "collect_time")
    private LocalDateTime collectTime;
    
    @Column(length = 500)
    private String url;
    
    @Column(length = 100)
    private String author;
    
    @Column(name = "image_count")
    private Integer imageCount;
    
    @Column(name = "video_count")
    private Integer videoCount;
    
    @Column(name = "word_count")
    private Integer wordCount;
    
    private Long likes;
    
    private Long comments;
    
    private Long shares;
    
    private Long views;
    
    @Column(length = 30)
    private String category;
    
    @Column(length = 20)
    private String sentiment;
    
    @Column(columnDefinition = "TEXT")
    private String tags;
    
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
        if (collectTime == null) {
            collectTime = LocalDateTime.now();
        }
    }
}
