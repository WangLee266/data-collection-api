package com.datacollection.service.impl;

import com.datacollection.entity.Organization;
import com.datacollection.repository.OrganizationRepository;
import com.datacollection.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    
    private final OrganizationRepository organizationRepository;
    
    @Override
    @Transactional
    public Organization addOrganization(Organization organization) {
        organization.setStatus(1);
        return organizationRepository.save(organization);
    }
    
    @Override
    @Transactional
    public Organization updateOrganization(Long id, Organization organization) {
        Organization existing = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("组织不存在"));
        organization.setId(id);
        return organizationRepository.save(organization);
    }
    
    @Override
    @Transactional
    public void deleteOrganization(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("组织不存在"));
        organization.setStatus(0);
        organizationRepository.save(organization);
    }
    
    @Override
    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("组织不存在"));
    }
    
    @Override
    public List<Organization> searchOrganizations(String keyword, String country, String type) {
        return organizationRepository.searchOrganizations(keyword, country, type);
    }
    
    @Override
    public Long countActive() {
        return organizationRepository.countActive();
    }
}
