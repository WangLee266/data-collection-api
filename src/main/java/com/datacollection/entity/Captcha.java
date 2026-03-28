package com.datacollection.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 验证码实体
 */
@Data
@Entity
@Table(name = "sys_captcha")
public class Captcha {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String uuid;
    
    @Column(nullable = false, length = 10)
    private String code;
    
    @Column(name = "expire_time")
    private LocalDateTime expireTime;
    
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
