package com.datacollection.service;

import com.datacollection.dto.*;
import com.datacollection.entity.User;

/**
 * 认证服务接口
 */
public interface AuthService {
    
    /**
     * 生成验证码
     */
    CaptchaResponse generateCaptcha();
    
    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request);
    
    /**
     * 刷新Token
     */
    LoginResponse refreshToken(String refreshToken);
    
    /**
     * 登出
     */
    void logout(String token);
    
    /**
     * 检查UKey
     */
    boolean checkUkey(String ukeySerial);
}
