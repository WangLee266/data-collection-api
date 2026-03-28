package com.datacollection.repository;

import com.datacollection.entity.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CaptchaRepository extends JpaRepository<Captcha, Long> {
    
    Optional<Captcha> findByUuid(String uuid);
    
    void deleteByUuid(String uuid);
}
