package com.datacollection.dto;

import lombok.Data;

/**
 * 社交平台订阅账号请求DTO
 */
@Data
public class SocialSubscriptionRequest {
    
    private String platform;
    
    private String platformLabel;
    
    private String accountName;
    
    private String password;
    
    private String email;
    
    private String emailPassword;
    
    private String twoFA;
    
    private String remark;
}
