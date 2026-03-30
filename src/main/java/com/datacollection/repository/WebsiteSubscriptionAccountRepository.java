package com.datacollection.repository;

import com.datacollection.entity.WebsiteSubscriptionAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 网站订阅账号Repository
 */
@Repository
public interface WebsiteSubscriptionAccountRepository extends JpaRepository<WebsiteSubscriptionAccount, Long> {
    
    /**
     * 按状态查询
     */
    List<WebsiteSubscriptionAccount> findByStatus(String status);
    
    /**
     * 按网站ID查询
     */
    List<WebsiteSubscriptionAccount> findByWebsiteId(Long websiteId);
    
    /**
     * 按网站名称模糊查询
     */
    Page<WebsiteSubscriptionAccount> findByWebsiteNameContaining(String keyword, Pageable pageable);
    
    /**
     * 统计各状态数量
     */
    long countByStatus(String status);
    
    /**
     * 查询即将到期的账号（30天内）
     */
    @Query("SELECT w FROM WebsiteSubscriptionAccount w WHERE w.expireDate BETWEEN :startDate AND :endDate AND w.status != '已过期'")
    List<WebsiteSubscriptionAccount> findExpiringAccounts(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * 按网站名称分组统计
     */
    @Query("SELECT w.websiteName, COUNT(w) FROM WebsiteSubscriptionAccount w GROUP BY w.websiteName")
    List<Object[]> countByWebsiteName();
    
    /**
     * 多条件查询
     */
    @Query("SELECT w FROM WebsiteSubscriptionAccount w WHERE " +
           "(:websiteName IS NULL OR w.websiteName LIKE %:websiteName%) AND " +
           "(:status IS NULL OR w.status = :status) AND " +
           "(:accountName IS NULL OR w.accountName LIKE %:accountName%)")
    Page<WebsiteSubscriptionAccount> search(@Param("websiteName") String websiteName,
                                            @Param("status") String status,
                                            @Param("accountName") String accountName,
                                            Pageable pageable);
}
