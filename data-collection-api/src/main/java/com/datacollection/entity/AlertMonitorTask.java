package com.datacollection.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 预警监测任务实体
 * 用于管理预警监测任务的配置
 */
@Data
@Entity
@Table(name = "alert_monitor_task")
public class AlertMonitorTask {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 任务名称
     */
    @Column(name = "task_name", nullable = false, length = 100)
    private String taskName;
    
    /**
     * 监测目标类型：website, social
     */
    @Column(name = "target_type", length = 20)
    private String targetType;
    
    /**
     * 监测目标ID列表（逗号分隔）
     */
    @Column(name = "target_ids", columnDefinition = "TEXT")
    private String targetIds;
    
    /**
     * 监测频率：15m, 30m, 1h, 2h, 6h, 12h, 24h
     */
    @Column(name = "frequency", length = 10)
    private String frequency;
    
    /**
     * 监测时间段开始
     */
    @Column(name = "monitor_time_start", length = 10)
    private String monitorTimeStart;
    
    /**
     * 监测时间段结束
     */
    @Column(name = "monitor_time_end", length = 10)
    private String monitorTimeEnd;
    
    /**
     * 无数据超时阈值
     */
    @Column(name = "no_data_threshold")
    private Integer noDataThreshold;
    
    /**
     * 无数据超时单位：m(分钟), h(小时), d(天)
     */
    @Column(name = "no_data_unit", length = 5)
    private String noDataUnit;
    
    /**
     * 采集成功率阈值（百分比）
     */
    @Column(name = "success_rate_threshold")
    private Integer successRateThreshold;
    
    /**
     * 数据量异常波动阈值（百分比）
     */
    @Column(name = "data_change_threshold")
    private Integer dataChangeThreshold;
    
    /**
     * 是否启用页面结构变化检测
     */
    @Column(name = "structure_change_detect")
    private Boolean structureChangeDetect;
    
    /**
     * 是否启用账号限流检测
     */
    @Column(name = "rate_limit_detect")
    private Boolean rateLimitDetect;
    
    /**
     * 通知方式：sys, email, wechat, sms（逗号分隔）
     */
    @Column(name = "notify_channels", length = 100)
    private String notifyChannels;
    
    /**
     * 状态：pending, running, paused, completed
     */
    @Column(length = 20)
    private String status;
    
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
    
    /**
     * 最后执行时间
     */
    @Column(name = "last_execute_time")
    private LocalDateTime lastExecuteTime;
    
    /**
     * 版本号
     */
    @Column
    private Integer version = 1;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = "pending";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
        version++;
    }
}
