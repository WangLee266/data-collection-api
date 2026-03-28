package com.datacollection.repository;

import com.datacollection.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    
    @Query("SELECT o FROM Organization o WHERE " +
           "(:keyword IS NULL OR o.nameCn LIKE %:keyword% OR o.nameEn LIKE %:keyword% OR o.shortName LIKE %:keyword%) AND " +
           "(:country IS NULL OR o.country = :country) AND " +
           "(:type IS NULL OR o.type = :type) AND " +
           "o.status = 1")
    List<Organization> searchOrganizations(String keyword, String country, String type);
    
    @Query("SELECT COUNT(o) FROM Organization o WHERE o.status = 1")
    Long countActive();
}
