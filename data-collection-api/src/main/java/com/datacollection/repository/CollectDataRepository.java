package com.datacollection.repository;

import com.datacollection.entity.CollectData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CollectDataRepository extends JpaRepository<CollectData, Long> {
    
    @Query("SELECT d FROM CollectData d WHERE " +
           "(:mainTab IS NULL OR d.mainTab = :mainTab) AND " +
           "(:sourceType IS NULL OR d.sourceType = :sourceType) AND " +
           "(:platform IS NULL OR d.platform = :platform) AND " +
           "(:keyword IS NULL OR d.title LIKE %:keyword% OR d.content LIKE %:keyword% OR d.source LIKE %:keyword%) AND " +
           "(:country IS NULL OR d.country = :country) AND " +
           "(:language IS NULL OR d.language = :language) AND " +
           "(:category IS NULL OR d.category = :category)")
    List<CollectData> searchData(String mainTab, String sourceType, String platform, 
                                  String keyword, String country, String language, String category);
    
    @Query("SELECT COUNT(d) FROM CollectData d")
    Long countTotal();
    
    @Query("SELECT COUNT(d) FROM CollectData d WHERE d.collectTime >= :startTime")
    Long countToday(LocalDateTime startTime);
    
    @Query("SELECT COUNT(d) FROM CollectData d WHERE d.mainTab = :mainTab")
    Long countByMainTab(String mainTab);
    
    @Query("SELECT SUM(d.views) FROM CollectData d WHERE d.mainTab = 'social'")
    Long sumSocialViews();
}
