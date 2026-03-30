package com.datacollection.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 社交账号实体
 */
@Data
@Entity
@Table(name = "source_social_account")
public class SocialAccount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 30)
    private String platform;
    
    @Column(nullable = false, unique = true, length = 255)
    private String url;
    
    @Column(name = "account_id", length = 100)
    private String accountId;
    
    @Column(length = 100)
    private String name;
    
    @Column(name = "display_name", length = 100)
    private String displayName;
    
    @Column(name = "owner_id")
    private Long ownerId;
    
    @Column(name = "owner_name", length = 100)
    private String ownerName;
    
    @Column(name = "owner_type", length = 20)
    private String ownerType;
    
    @Column(length = 20)
    private String domain;
    
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
