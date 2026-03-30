package com.datacollection.service.impl;

import com.datacollection.entity.SocialAccount;
import com.datacollection.repository.SocialAccountRepository;
import com.datacollection.service.SocialAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialAccountServiceImpl implements SocialAccountService {
    
    private final SocialAccountRepository socialAccountRepository;
    
    @Override
    @Transactional
    public SocialAccount addAccount(SocialAccount account) {
        if (socialAccountRepository.findByUrl(account.getUrl()).isPresent()) {
            throw new RuntimeException("账号URL已存在");
        }
        account.setVersion(1);
        account.setStatus(1);
        return socialAccountRepository.save(account);
    }
    
    @Override
    @Transactional
    public void batchImport(List<SocialAccount> accounts) {
        for (SocialAccount account : accounts) {
            if (socialAccountRepository.findByUrl(account.getUrl()).isEmpty()) {
                account.setVersion(1);
                account.setStatus(1);
                socialAccountRepository.save(account);
            }
        }
    }
    
    @Override
    @Transactional
    public SocialAccount updateAccount(Long id, SocialAccount account) {
        SocialAccount existing = socialAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("账号不存在"));
        account.setId(id);
        return socialAccountRepository.save(account);
    }
    
    @Override
    @Transactional
    public void deleteAccount(Long id) {
        SocialAccount account = socialAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("账号不存在"));
        account.setStatus(0);
        socialAccountRepository.save(account);
    }
    
    @Override
    public SocialAccount getAccountById(Long id) {
        return socialAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("账号不存在"));
    }
    
    @Override
    public List<SocialAccount> searchAccounts(String keyword, String platform, String domain) {
        return socialAccountRepository.searchAccounts(keyword, platform, domain);
    }
    
    @Override
    public List<SocialAccount> getAccountsByPlatform(String platform) {
        return socialAccountRepository.findByPlatform(platform);
    }
    
    @Override
    public Long countActive() {
        return socialAccountRepository.countActive();
    }
}
