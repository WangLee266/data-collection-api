package com.datacollection.service.impl;

import com.datacollection.dto.*;
import com.datacollection.entity.Captcha;
import com.datacollection.entity.User;
import com.datacollection.repository.CaptchaRepository;
import com.datacollection.repository.UserRepository;
import com.datacollection.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    @Value("${datacollection.captcha.length:5}")
    private Integer captchaLength;
    
    @Value("${datacollection.captcha.expire-seconds:300}")
    private Integer captchaExpireSeconds;
    
    @Override
    public CaptchaResponse generateCaptcha() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < captchaLength; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        String uuid = java.util.UUID.randomUUID().toString();
        Captcha captcha = new Captcha();
        captcha.setUuid(uuid);
        captcha.setCode(code.toString());
        captcha.setExpireTime(LocalDateTime.now().plusSeconds(captchaExpireSeconds));
        captchaRepository.save(captcha);
        
        CaptchaResponse response = new CaptchaResponse();
        response.setUuid(uuid);
        response.setCaptcha(code.toString());
        response.setExpireSeconds(captchaExpireSeconds);
        return response;
    }
    
    @Override
    public LoginResponse login(LoginRequest request) {
        // 验证验证码
        Captcha captcha = captchaRepository.findByUuid(request.getCaptchaUuid())
                .orElseThrow(() -> new RuntimeException("验证码已过期"));
        
        if (captcha.isExpired()) {
            captchaRepository.delete(captcha);
            throw new RuntimeException("验证码已过期");
        }
        
        if (!captcha.getCode().equalsIgnoreCase(request.getCaptcha())) {
            throw new RuntimeException("验证码错误");
        }
        
        captchaRepository.delete(captcha);
        
        // 验证用户
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
        
        if (user.getStatus() != 1) {
            throw new RuntimeException("账号已被禁用");
        }
        
        // 验证密码 (实际项目中应该使用加密)
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        // 更新登录信息
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);
        
        // 生成Token
        String token = generateToken(user.getUsername());
        String refreshToken = generateRefreshToken(user.getUsername());
        
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(jwtExpiration);
        
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRealName(user.getRealName());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());
        userInfo.setRole(user.getRole());
        response.setUserInfo(userInfo);
        
        return response;
    }
    
    @Override
    public LoginResponse refreshToken(String refreshToken) {
        // 验证refresh token并生成新token
        String username = extractUsername(refreshToken);
        String newToken = generateToken(username);
        String newRefreshToken = generateRefreshToken(username);
        
        LoginResponse response = new LoginResponse();
        response.setToken(newToken);
        response.setRefreshToken(newRefreshToken);
        response.setExpiresIn(jwtExpiration);
        return response;
    }
    
    @Override
    public void logout(String token) {
        // 可以将token加入黑名单
    }
    
    @Override
    public boolean checkUkey(String ukeySerial) {
        if (ukeySerial == null || ukeySerial.isEmpty()) {
            return false;
        }
        return userRepository.findByUkeySerial(ukeySerial).isPresent();
    }
    
    private String generateToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    private String generateRefreshToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration * 7))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    private String extractUsername(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
