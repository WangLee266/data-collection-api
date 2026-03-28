package com.datacollection.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 网站信源实体
 */
@Data
@Entity
@Table(name = "source_website")
public class Website {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 255)
    private String url;
    
    @Column(name = "name_cn", length = 100)
    private String nameCn;
    
    @Column(name = "name_en", length = 100)
    private String nameEn;
    
    @Column(name = "name_origin", length = 100)
    private String nameOrigin;
    
    @Column(length = 20)
    private String language;
    
    @Column(length = 20)
    private String type;
    
    @Column(length = 20)
    private String domain;
    
    @Column(name = "owner_id")
    private Long ownerId;
    
    @Column(name = "owner_name", length = 100)
    private String ownerName;
    
    @Column(name = "source_type", length = 20)
    private String sourceType;
    
    @Column
    private Integer version = 1;
    
    @Column(nullable = false)
    private Integer status = 1;
    
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
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
        version++;
    }
}
