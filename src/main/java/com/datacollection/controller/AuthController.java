package com.datacollection.controller;

import com.datacollection.dto.*;
import com.datacollection.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Tag(name = "认证管理", description = "登录、验证码、Token刷新等接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @Operation(summary = "获取验证码")
    @GetMapping("/captcha")
    public ApiResponse<CaptchaResponse> getCaptcha() {
        return ApiResponse.success(authService.generateCaptcha());
    }
    
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success("登录成功", authService.login(request));
    }
    
    @Operation(summary = "刷新Token")
    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        return ApiResponse.success(authService.refreshToken(refreshToken));
    }
    
    @Operation(summary = "登出")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ApiResponse.success("登出成功");
    }
    
    @Operation(summary = "检查UKey状态")
    @GetMapping("/ukey/check")
    public ApiResponse<Boolean> checkUkey(@RequestParam String serial) {
        return ApiResponse.success(authService.checkUkey(serial));
    }
}
