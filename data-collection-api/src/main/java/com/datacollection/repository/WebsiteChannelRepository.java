package com.datacollection.repository;

import com.datacollection.entity.WebsiteChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WebsiteChannelRepository extends JpaRepository<WebsiteChannel, Long> {
    
    List<WebsiteChannel> findByWebsiteId(Long websiteId);
    
    void deleteByWebsiteId(Long websiteId);
}
