package com.datacollection.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * 网站订阅账号请求DTO
 */
@Data
public class WebsiteSubscriptionRequest {
    
    private Long websiteId;
    
    private String websiteName;
    
    private String websiteUrl;
    
    private String accountName;
    
    private String password;
    
    private LocalDate expireDate;
    
    private String chargeInfo;
    
    private String remark;
}
