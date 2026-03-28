package com.datacollection.dto;

import lombok.Data;

/**
 * 验证码响应
 */
@Data
public class CaptchaResponse {
    
    private String uuid;
    private String captcha;
    private Integer expireSeconds;
}
