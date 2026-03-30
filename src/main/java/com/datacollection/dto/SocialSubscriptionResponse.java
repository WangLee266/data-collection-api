package com.datacollection.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 社交平台订阅账号响应DTO
 */
@Data
public class SocialSubscriptionResponse {
    
    private Long id;
    
    private String platform;
    
    private String platformLabel;
    
    private String accountName;
    
    private String password;
    
    private String email;
    
    private String emailPassword;
    
    private String twoFA;
    
    private String status;
    
    private String remark;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}
