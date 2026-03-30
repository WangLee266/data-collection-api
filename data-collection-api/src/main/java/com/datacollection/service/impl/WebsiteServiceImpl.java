package com.datacollection.service.impl;

import com.datacollection.dto.*;
import com.datacollection.entity.Website;
import com.datacollection.repository.WebsiteRepository;
import com.datacollection.service.WebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebsiteServiceImpl implements WebsiteService {
    
    private final WebsiteRepository websiteRepository;
    
    @Override
    @Transactional
    public Website addWebsite(Website website) {
        if (websiteRepository.findByUrl(website.getUrl()).isPresent()) {
            throw new RuntimeException("网站URL已存在");
        }
        website.setVersion(1);
        website.setStatus(1);
        return websiteRepository.save(website);
    }
    
    @Override
    @Transactional
    public void batchImport(List<Website> websites) {
        for (Website website : websites) {
            if (websiteRepository.findByUrl(website.getUrl()).isEmpty()) {
                website.setVersion(1);
                website.setStatus(1);
                websiteRepository.save(website);
            }
        }
    }
    
    @Override
    @Transactional
    public Website updateWebsite(Long id, Website website) {
        Website existing = websiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("网站不存在"));
        website.setId(id);
        return websiteRepository.save(website);
    }
    
    @Override
    @Transactional
    public void deleteWebsite(Long id) {
        Website website = websiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("网站不存在"));
        website.setStatus(0);
        websiteRepository.save(website);
    }
    
    @Override
    public Website getWebsiteById(Long id) {
        return websiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("网站不存在"));
    }
    
    @Override
    public List<Website> searchWebsites(String keyword, String language, String domain, String sourceType) {
        return websiteRepository.searchWebsites(keyword, language, domain, sourceType);
    }
    
    @Override
    public Long countActive() {
        return websiteRepository.countActive();
    }
}
