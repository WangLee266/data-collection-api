package com.datacollection.repository;

import com.datacollection.entity.SocialSubscriptionAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 社交平台订阅账号Repository
 */
@Repository
public interface SocialSubscriptionAccountRepository extends JpaRepository<SocialSubscriptionAccount, Long> {
    
    /**
     * 按状态查询
     */
    List<SocialSubscriptionAccount> findByStatus(String status);
    
    /**
     * 按平台查询
     */
    List<SocialSubscriptionAccount> findByPlatform(String platform);
    
    /**
     * 按账号名称模糊查询
     */
    Page<SocialSubscriptionAccount> findByAccountNameContaining(String keyword, Pageable pageable);
    
    /**
     * 统计各状态数量
     */
    long countByStatus(String status);
    
    /**
     * 按平台统计数量
     */
    long countByPlatform(String platform);
    
    /**
     * 按平台分组统计
     */
    @Query("SELECT s.platform, s.platformLabel, COUNT(s) FROM SocialSubscriptionAccount s GROUP BY s.platform, s.platformLabel")
    List<Object[]> countByPlatform();
    
    /**
     * 检查2FA是否设置
     */
    @Query("SELECT COUNT(s) FROM SocialSubscriptionAccount s WHERE s.twoFA IS NOT NULL AND s.twoFA != ''")
    long countWithTwoFA();
    
    /**
     * 多条件查询
     */
    @Query("SELECT s FROM SocialSubscriptionAccount s WHERE " +
           "(:platform IS NULL OR s.platform = :platform) AND " +
           "(:status IS NULL OR s.status = :status) AND " +
           "(:accountName IS NULL OR s.accountName LIKE %:accountName%)")
    Page<SocialSubscriptionAccount> search(@Param("platform") String platform,
                                           @Param("status") String status,
                                           @Param("accountName") String accountName,
                                           Pageable pageable);
}
