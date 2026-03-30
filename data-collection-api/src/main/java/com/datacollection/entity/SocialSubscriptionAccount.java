package com.datacollection.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 社交平台订阅账号实体
 * 用于管理社交平台账号的订阅信息
 */
@Data
@Entity
@Table(name = "subscription_social_account")
public class SocialSubscriptionAccount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 平台标识：x, facebook, instagram, telegram, youtube, tiktok, linkedin, reddit, vk, whatsapp
     */
    @Column(nullable = false, length = 30)
    private String platform;
    
    /**
     * 平台显示名称
     */
    @Column(name = "platform_label", length = 50)
    private String platformLabel;
    
    /**
     * 账号名称/用户名
     */
    @Column(name = "account_name", nullable = false, length = 100)
    private String accountName;
    
    /**
     * 密码（加密存储）
     */
    @Column(name = "password", length = 255)
    private String password;
    
    /**
     * 绑定邮箱地址
     */
    @Column(name = "email", length = 100)
    private String email;
    
    /**
     * 邮箱密码（加密存储）
     */
    @Column(name = "email_password", length = 255)
    private String emailPassword;
    
    /**
     * 二步验证码/密钥
     */
    @Column(name = "two_fa", length = 100)
    private String twoFA;
    
    /**
     * 状态：有效、受限、封禁、已过期
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
    }
}
