package com.datacollection.repository;

import com.datacollection.entity.NotificationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 通知配置Repository
 */
@Repository
public interface NotificationConfigRepository extends JpaRepository<NotificationConfig, Long> {
    
    /**
     * 按配置类型查询
     */
    List<NotificationConfig> findByConfigType(String configType);
    
    /**
     * 按配置类型和启用状态查询
     */
    List<NotificationConfig> findByConfigTypeAndIsEnabled(String configType, Boolean isEnabled);
    
    /**
     * 查询所有启用的配置
     */
    @Query("SELECT n FROM NotificationConfig n WHERE n.isEnabled = true ORDER BY n.sortOrder")
    List<NotificationConfig> findAllEnabled();
    
    /**
     * 按配置类型查询启用的配置
     */
    @Query("SELECT n FROM NotificationConfig n WHERE n.configType = :configType AND n.isEnabled = true ORDER BY n.sortOrder")
    List<NotificationConfig> findEnabledByType(@Param("configType") String configType);
    
    /**
     * 检查配置是否存在
     */
    boolean existsByConfigTypeAndConfigName(String configType, String configName);
}
