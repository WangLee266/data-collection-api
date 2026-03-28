package com.datacollection.repository;

import com.datacollection.entity.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    
    Optional<SocialAccount> findByUrl(String url);
    
    Optional<SocialAccount> findByAccountId(String accountId);
    
    List<SocialAccount> findByPlatform(String platform);
    
    @Query("SELECT s FROM SocialAccount s WHERE " +
           "(:keyword IS NULL OR s.name LIKE %:keyword% OR s.displayName LIKE %:keyword% OR s.accountId LIKE %:keyword% OR s.ownerName LIKE %:keyword%) AND " +
           "(:platform IS NULL OR s.platform = :platform) AND " +
           "(:domain IS NULL OR s.domain = :domain) AND " +
           "s.status = 1")
    List<SocialAccount> searchAccounts(String keyword, String platform, String domain);
    
    @Query("SELECT COUNT(s) FROM SocialAccount s WHERE s.status = 1")
    Long countActive();
    
    @Query("SELECT s.platform, COUNT(s) FROM SocialAccount s WHERE s.status = 1 GROUP BY s.platform")
    List<Object[]> countByPlatform();
}
