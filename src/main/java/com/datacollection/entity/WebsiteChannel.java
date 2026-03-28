package com.datacollection.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 网站栏目实体
 */
@Data
@Entity
@Table(name = "source_website_channel")
public class WebsiteChannel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "website_id", nullable = false)
    private Long websiteId;
    
    @Column(nullable = false, length = 255)
    private String url;
    
    @Column(name = "name_cn", length = 100)
    private String nameCn;
    
    @Column(name = "name_en", length = 100)
    private String nameEn;
    
    @Column(length = 20)
    private String domain;
    
    @Column(name = "collect_level")
    private Integer collectLevel = 2;
    
    @Column(nullable = false)
    private Integer status = 1;
    
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
