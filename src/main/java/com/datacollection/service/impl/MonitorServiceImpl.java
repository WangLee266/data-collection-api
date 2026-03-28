package com.datacollection.service.impl;

import com.datacollection.dto.*;
import com.datacollection.entity.Alert;
import com.datacollection.enums.AlertLevel;
import com.datacollection.enums.AlertStatus;
import com.datacollection.repository.AlertRepository;
import com.datacollection.service.MonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonitorServiceImpl implements MonitorService {
    
    private final AlertRepository alertRepository;
    
    @Override
    public List<Alert> getAlerts(String status, String level) {
        AlertStatus alertStatus = status != null ? AlertStatus.valueOf(status.toUpperCase()) : null;
        AlertLevel alertLevel = level != null ? AlertLevel.valueOf(level.toUpperCase()) : null;
        return alertRepository.searchAlerts(alertStatus, alertLevel, LocalDate.now().atStartOfDay());
    }
    
    @Override
    public Alert getAlertById(Long id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("预警不存在"));
    }
    
    @Override
    @Transactional
    public void processAlert(Long id, String action) {
        Alert alert = getAlertById(id);
        alert.setStatus(AlertStatus.PROCESSING);
        alertRepository.save(alert);
    }
    
    @Override
    @Transactional
    public void resolveAlert(Long id, String resolveUser) {
        Alert alert = getAlertById(id);
        alert.setStatus(AlertStatus.RESOLVED);
        alert.setResolveTime(LocalDateTime.now());
        alert.setResolveUser(resolveUser);
        alertRepository.save(alert);
    }
    
    @Override
    public MonitorStatsResponse getMonitorStats() {
        MonitorStatsResponse stats = new MonitorStatsResponse();
        stats.setPendingAlerts(alertRepository.countPending());
        stats.setProcessingCount(alertRepository.countProcessing());
        stats.setTodayTotal(alertRepository.countToday(LocalDate.now().atStartOfDay()));
        stats.setTodayResolved(alertRepository.countTodayResolved(LocalDate.now().atStartOfDay()));
        return stats;
    }
    
    @Override
    @Transactional
    public Alert createAlert(Alert alert) {
        return alertRepository.save(alert);
    }
}
