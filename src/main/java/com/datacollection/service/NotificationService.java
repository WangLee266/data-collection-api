package com.datacollection.service;

import com.datacollection.dto.NotificationConfigRequest;
import com.datacollection.dto.NotificationConfigResponse;
import com.datacollection.entity.NotificationConfig;
import com.datacollection.repository.NotificationConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 通知配置服务
 */
@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationConfigRepository configRepository;
    
    /**
     * 创建通知配置
     */
    @Transactional
    public NotificationConfigResponse createConfig(NotificationConfigRequest request) {
        NotificationConfig config = new NotificationConfig();
        copyProperties(request, config);
        config = configRepository.save(config);
        return convertToResponse(config);
    }
    
    /**
     * 更新通知配置
     */
    @Transactional
    public NotificationConfigResponse updateConfig(Long id, NotificationConfigRequest request) {
        NotificationConfig config = configRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("通知配置不存在: " + id));
        copyProperties(request, config);
        config = configRepository.save(config);
        return convertToResponse(config);
    }
    
    /**
     * 删除通知配置
     */
    @Transactional
    public void deleteConfig(Long id) {
        configRepository.deleteById(id);
    }
    
    /**
     * 获取通知配置详情
     */
    public NotificationConfigResponse getConfig(Long id) {
        NotificationConfig config = configRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("通知配置不存在: " + id));
        return convertToResponse(config);
    }
    
    /**
     * 按类型查询配置列表
     */
    public List<NotificationConfigResponse> listByType(String configType) {
        List<NotificationConfig> configs;
        if (configType != null && !configType.isEmpty()) {
            configs = configRepository.findByConfigType(configType);
        } else {
            configs = configRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder"));
        }
        return configs.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * 获取启用的配置
     */
    public List<NotificationConfigResponse> listEnabled() {
        return configRepository.findAllEnabled().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * 启用/禁用配置
     */
    @Transactional
    public NotificationConfigResponse toggleConfig(Long id, Boolean enabled) {
        NotificationConfig config = configRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("通知配置不存在: " + id));
        config.setIsEnabled(enabled);
        config = configRepository.save(config);
        return convertToResponse(config);
    }
    
    // ============= 私有方法 =============
    
    private void copyProperties(NotificationConfigRequest request, NotificationConfig config) {
        config.setConfigType(request.getConfigType());
        config.setIsEnabled(request.getIsEnabled());
        config.setAlertLevel(request.getAlertLevel());
        config.setConfigName(request.getConfigName());
        config.setConfigValue(request.getConfigValue());
        config.setExtraConfig(request.getExtraConfig());
        config.setSortOrder(request.getSortOrder());
    }
    
    private NotificationConfigResponse convertToResponse(NotificationConfig config) {
        NotificationConfigResponse response = new NotificationConfigResponse();
        response.setId(config.getId());
        response.setConfigType(config.getConfigType());
        response.setIsEnabled(config.getIsEnabled());
        response.setAlertLevel(config.getAlertLevel());
        response.setConfigName(config.getConfigName());
        response.setConfigValue(config.getConfigValue());
        response.setExtraConfig(config.getExtraConfig());
        response.setSortOrder(config.getSortOrder());
        response.setCreateTime(config.getCreateTime());
        response.setUpdateTime(config.getUpdateTime());
        return response;
    }
}
