package com.datacollection.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 通知配置实体
 * 用于管理各种通知方式的配置
 */
@Data
@Entity
@Table(name = "notification_config")
public class NotificationConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 配置类型：sys(系统消息), email(邮件), wechat(企业微信), sms(短信)
     */
    @Column(name = "config_type", nullable = false, length = 20)
    private String configType;
    
    /**
     * 是否启用
     */
    @Column(name = "is_enabled")
    private Boolean isEnabled;
    
    /**
     * 通知级别：all(所有), serious(严重及以上), critical(仅紧急)
     */
    @Column(name = "alert_level", length = 20)
    private String alertLevel;
    
    /**
     * 配置名称
     */
    @Column(name = "config_name", length = 100)
    private String configName;
    
    /**
     * 配置值（如邮箱地址、手机号、webhook URL等）
     */
    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;
    
    /**
     * 扩展配置（JSON格式，用于存储额外参数）
     */
    @Column(name = "extra_config", columnDefinition = "TEXT")
    private String extraConfig;
    
    /**
     * 排序号
     */
    @Column(name = "sort_order")
    private Integer sortOrder;
    
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (isEnabled == null) {
            isEnabled = true;
        }
        if (alertLevel == null) {
            alertLevel = "all";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
