package com.datacollection.service;

import com.datacollection.dto.SubscriptionStatsResponse;
import com.datacollection.dto.WebsiteSubscriptionRequest;
import com.datacollection.dto.WebsiteSubscriptionResponse;
import com.datacollection.entity.WebsiteSubscriptionAccount;
import com.datacollection.repository.WebsiteSubscriptionAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订阅账号管理服务
 */
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    
    private final WebsiteSubscriptionAccountRepository websiteAccountRepository;
    private final com.datacollection.repository.SocialSubscriptionAccountRepository socialAccountRepository;
    
    // ============= 网站订阅账号管理 =============
    
    /**
     * 创建网站订阅账号
     */
    @Transactional
    public WebsiteSubscriptionResponse createWebsiteAccount(WebsiteSubscriptionRequest request) {
        WebsiteSubscriptionAccount account = new WebsiteSubscriptionAccount();
        copyWebsiteProperties(request, account);
        account = websiteAccountRepository.save(account);
        return convertToWebsiteResponse(account);
    }
    
    /**
     * 更新网站订阅账号
     */
    @Transactional
    public WebsiteSubscriptionResponse updateWebsiteAccount(Long id, WebsiteSubscriptionRequest request) {
        WebsiteSubscriptionAccount account = websiteAccountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("网站订阅账号不存在: " + id));
        copyWebsiteProperties(request, account);
        account = websiteAccountRepository.save(account);
        return convertToWebsiteResponse(account);
    }
    
    /**
     * 删除网站订阅账号
     */
    @Transactional
    public void deleteWebsiteAccount(Long id) {
        websiteAccountRepository.deleteById(id);
    }
    
    /**
     * 获取网站订阅账号详情
     */
    public WebsiteSubscriptionResponse getWebsiteAccount(Long id) {
        WebsiteSubscriptionAccount account = websiteAccountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("网站订阅账号不存在: " + id));
        return convertToWebsiteResponse(account);
    }
    
    /**
     * 分页查询网站订阅账号
     */
    public Page<WebsiteSubscriptionResponse> listWebsiteAccounts(String websiteName, String status, 
                                                                  String accountName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<WebsiteSubscriptionAccount> accounts = websiteAccountRepository.search(websiteName, status, accountName, pageable);
        return accounts.map(this::convertToWebsiteResponse);
    }
    
    // ============= 社交平台订阅账号管理 =============
    
    /**
     * 创建社交平台订阅账号
     */
    @Transactional
    public com.datacollection.dto.SocialSubscriptionResponse createSocialAccount(com.datacollection.dto.SocialSubscriptionRequest request) {
        com.datacollection.entity.SocialSubscriptionAccount account = new com.datacollection.entity.SocialSubscriptionAccount();
        copySocialProperties(request, account);
        account = socialAccountRepository.save(account);
        return convertToSocialResponse(account);
    }
    
    /**
     * 更新社交平台订阅账号
     */
    @Transactional
    public com.datacollection.dto.SocialSubscriptionResponse updateSocialAccount(Long id, com.datacollection.dto.SocialSubscriptionRequest request) {
        com.datacollection.entity.SocialSubscriptionAccount account = socialAccountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("社交平台订阅账号不存在: " + id));
        copySocialProperties(request, account);
        account = socialAccountRepository.save(account);
        return convertToSocialResponse(account);
    }
    
    /**
     * 删除社交平台订阅账号
     */
    @Transactional
    public void deleteSocialAccount(Long id) {
        socialAccountRepository.deleteById(id);
    }
    
    /**
     * 获取社交平台订阅账号详情
     */
    public com.datacollection.dto.SocialSubscriptionResponse getSocialAccount(Long id) {
        com.datacollection.entity.SocialSubscriptionAccount account = socialAccountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("社交平台订阅账号不存在: " + id));
        return convertToSocialResponse(account);
    }
    
    /**
     * 分页查询社交平台订阅账号
     */
    public Page<com.datacollection.dto.SocialSubscriptionResponse> listSocialAccounts(String platform, String status,
                                                                                       String accountName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<com.datacollection.entity.SocialSubscriptionAccount> accounts = socialAccountRepository.search(platform, status, accountName, pageable);
        return accounts.map(this::convertToSocialResponse);
    }
    
    // ============= 统计功能 =============
    
    /**
     * 获取订阅账号统计信息
     */
    public SubscriptionStatsResponse getStats() {
        SubscriptionStatsResponse stats = new SubscriptionStatsResponse();
        
        // 网站账号统计
        stats.setWebsiteAccountTotal((int) websiteAccountRepository.count());
        stats.setWebsiteAccountValid((int) websiteAccountRepository.countByStatus("有效"));
        stats.setWebsiteAccountExpired((int) websiteAccountRepository.countByStatus("已过期"));
        stats.setWebsiteAccountExpiring((int) websiteAccountRepository.countByStatus("即将到期"));
        
        // 社交账号统计
        stats.setSocialAccountTotal((int) socialAccountRepository.count());
        stats.setSocialAccountValid((int) socialAccountRepository.countByStatus("有效"));
        stats.setSocialAccountLimited((int) socialAccountRepository.countByStatus("受限"));
        stats.setSocialAccountBanned((int) socialAccountRepository.countByStatus("封禁"));
        
        // 平台分布
        List<Object[]> websiteDist = websiteAccountRepository.countByWebsiteName();
        stats.setWebsiteDistribution(websiteDist.stream()
            .map(arr -> {
                SubscriptionStatsResponse.PlatformDistribution pd = new SubscriptionStatsResponse.PlatformDistribution();
                pd.setName((String) arr[0]);
                pd.setCount(((Number) arr[1]).intValue());
                return pd;
            })
            .collect(Collectors.toList()));
        
        List<Object[]> socialDist = socialAccountRepository.countByPlatform();
        stats.setSocialDistribution(socialDist.stream()
            .map(arr -> {
                SubscriptionStatsResponse.PlatformDistribution pd = new SubscriptionStatsResponse.PlatformDistribution();
                pd.setName((String) arr[1]); // platformLabel
                pd.setCount(((Number) arr[2]).intValue());
                return pd;
            })
            .collect(Collectors.toList()));
        
        return stats;
    }
    
    // ============= 私有方法 =============
    
    private void copyWebsiteProperties(WebsiteSubscriptionRequest request, WebsiteSubscriptionAccount account) {
        account.setWebsiteId(request.getWebsiteId());
        account.setWebsiteName(request.getWebsiteName());
        account.setWebsiteUrl(request.getWebsiteUrl());
        account.setAccountName(request.getAccountName());
        account.setPassword(request.getPassword());
        account.setExpireDate(request.getExpireDate());
        account.setChargeInfo(request.getChargeInfo());
        account.setRemark(request.getRemark());
    }
    
    private void copySocialProperties(com.datacollection.dto.SocialSubscriptionRequest request, 
                                     com.datacollection.entity.SocialSubscriptionAccount account) {
        account.setPlatform(request.getPlatform());
        account.setPlatformLabel(request.getPlatformLabel());
        account.setAccountName(request.getAccountName());
        account.setPassword(request.getPassword());
        account.setEmail(request.getEmail());
        account.setEmailPassword(request.getEmailPassword());
        account.setTwoFA(request.getTwoFA());
        account.setRemark(request.getRemark());
    }
    
    private WebsiteSubscriptionResponse convertToWebsiteResponse(WebsiteSubscriptionAccount account) {
        WebsiteSubscriptionResponse response = new WebsiteSubscriptionResponse();
        response.setId(account.getId());
        response.setWebsiteId(account.getWebsiteId());
        response.setWebsiteName(account.getWebsiteName());
        response.setWebsiteUrl(account.getWebsiteUrl());
        response.setAccountName(account.getAccountName());
        response.setPassword(account.getPassword());
        response.setExpireDate(account.getExpireDate());
        response.setChargeInfo(account.getChargeInfo());
        response.setStatus(account.getStatus());
        response.setRemark(account.getRemark());
        response.setCreateTime(account.getCreateTime());
        response.setUpdateTime(account.getUpdateTime());
        return response;
    }
    
    private com.datacollection.dto.SocialSubscriptionResponse convertToSocialResponse(
            com.datacollection.entity.SocialSubscriptionAccount account) {
        com.datacollection.dto.SocialSubscriptionResponse response = new com.datacollection.dto.SocialSubscriptionResponse();
        response.setId(account.getId());
        response.setPlatform(account.getPlatform());
        response.setPlatformLabel(account.getPlatformLabel());
        response.setAccountName(account.getAccountName());
        response.setPassword(account.getPassword());
        response.setEmail(account.getEmail());
        response.setEmailPassword(account.getEmailPassword());
        response.setTwoFA(account.getTwoFA());
        response.setStatus(account.getStatus());
        response.setRemark(account.getRemark());
        response.setCreateTime(account.getCreateTime());
        response.setUpdateTime(account.getUpdateTime());
        return response;
    }
}
