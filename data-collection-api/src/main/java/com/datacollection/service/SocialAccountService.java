package com.datacollection.service;

import com.datacollection.entity.SocialAccount;
import java.util.List;

/**
 * 社交账号服务接口
 */
public interface SocialAccountService {
    
    /**
     * 添加账号
     */
    SocialAccount addAccount(SocialAccount account);
    
    /**
     * 批量导入账号
     */
    void batchImport(List<SocialAccount> accounts);
    
    /**
     * 更新账号
     */
    SocialAccount updateAccount(Long id, SocialAccount account);
    
    /**
     * 删除账号
     */
    void deleteAccount(Long id);
    
    /**
     * 获取账号详情
     */
    SocialAccount getAccountById(Long id);
    
    /**
     * 搜索账号
     */
    List<SocialAccount> searchAccounts(String keyword, String platform, String domain);
    
    /**
     * 按平台获取账号
     */
    List<SocialAccount> getAccountsByPlatform(String platform);
    
    /**
     * 获取活跃账号数量
     */
    Long countActive();
}
