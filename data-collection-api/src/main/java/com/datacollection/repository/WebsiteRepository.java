package com.datacollection.repository;

import com.datacollection.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WebsiteRepository extends JpaRepository<Website, Long> {
    
    Optional<Website> findByUrl(String url);
    
    @Query("SELECT w FROM Website w WHERE " +
           "(:keyword IS NULL OR w.nameCn LIKE %:keyword% OR w.nameEn LIKE %:keyword% OR w.url LIKE %:keyword%) AND " +
           "(:language IS NULL OR w.language = :language) AND " +
           "(:domain IS NULL OR w.domain = :domain) AND " +
           "(:sourceType IS NULL OR w.sourceType = :sourceType) AND " +
           "w.status = 1")
    List<Website> searchWebsites(String keyword, String language, String domain, String sourceType);
    
    @Query("SELECT COUNT(w) FROM Website w WHERE w.status = 1")
    Long countActive();
    
    @Query("SELECT w.language, COUNT(w) FROM Website w WHERE w.status = 1 GROUP BY w.language")
    List<Object[]> countByLanguage();
    
    @Query("SELECT w.sourceType, COUNT(w) FROM Website w WHERE w.status = 1 GROUP BY w.sourceType")
    List<Object[]> countBySourceType();
}
