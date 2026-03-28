package com.datacollection.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 人物实体
 */
@Data
@Entity
@Table(name = "source_person")
public class Person {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name_cn", length = 100)
    private String nameCn;
    
    @Column(name = "name_en", length = 100)
    private String nameEn;
    
    @Column(name = "name_origin", length = 100)
    private String nameOrigin;
    
    @Column(length = 50)
    private String country;
    
    @Column(length = 30)
    private String role;
    
    @Column(length = 100)
    private String avatar;
    
    @Column(columnDefinition = "TEXT")
    private String tags;
    
    @Column(name = "website_count")
    private Integer websiteCount = 0;
    
    @Column(name = "account_count")
    private Integer accountCount = 0;
    
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
    }
}
