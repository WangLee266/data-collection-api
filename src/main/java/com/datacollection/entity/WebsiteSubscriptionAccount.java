package com.datacollection.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 网站订阅账号实体
 * 用于管理网站付费订阅账号信息
 */
@Data
@Entity
@Table(name = "subscription_website_account")
public class WebsiteSubscriptionAccount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 关联的网站ID（来自信源管理）
     */
    @Column(name = "website_id")
    private Long websiteId;
    
    /**
     * 网站名称
     */
    @Column(name = "website_name", length = 100)
    private String websiteName;
    
    /**
     * 网站URL
     */
    @Column(name = "website_url", length = 255)
    private String websiteUrl;
    
    /**
     * 账号名称/登录账号
     */
    @Column(name = "account_name", nullable = false, length = 100)
    private String accountName;
    
    /**
     * 密码（加密存储）
     */
    @Column(name = "password", length = 255)
    private String password;
    
    /**
     * 有效截止日期
     */
    @Column(name = "expire_date")
    private LocalDate expireDate;
    
    /**
     * 收费信息（如：年费 $299）
     */
    @Column(name = "charge_info", length = 100)
    private String chargeInfo;
    
    /**
     * 状态：有效、已过期、即将到期
     */
    @Column(length = 20)
    private String status;
    
    /**
     * 备注
     */
    @Column(columnDefinition = "TEXT")
    private String remark;
    
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
     * 版本号
     */
    @Column
    private Integer version = 1;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = "有效";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
        version++;
        // 根据截止日期自动更新状态
        if (expireDate != null) {
            LocalDate now = LocalDate.now();
            if (expireDate.isBefore(now)) {
                status = "已过期";
            } else if (expireDate.isBefore(now.plusDays(30))) {
                status = "即将到期";
            } else {
                status = "有效";
            }
        }
    }
}
