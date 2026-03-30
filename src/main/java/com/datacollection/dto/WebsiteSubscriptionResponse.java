package com.datacollection.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 网站订阅账号响应DTO
 */
@Data
public class WebsiteSubscriptionResponse {
    
    private Long id;
    
    private Long websiteId;
    
    private String websiteName;
    
    private String websiteUrl;
    
    private String accountName;
    
    private String password;
    
    private LocalDate expireDate;
    
    private String chargeInfo;
    
    private String status;
    
    private String remark;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}
