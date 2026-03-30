package com.datacollection.service;

import com.datacollection.dto.AlertMonitorTaskRequest;
import com.datacollection.dto.AlertMonitorTaskResponse;
import com.datacollection.dto.AlertRankingResponse;
import com.datacollection.entity.AlertMonitorTask;
import com.datacollection.repository.AlertMonitorTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 预警监测任务服务
 */
@Service
@RequiredArgsConstructor
public class AlertMonitorService {
    
    private final AlertMonitorTaskRepository taskRepository;
    
    /**
     * 创建预警监测任务
     */
    @Transactional
    public AlertMonitorTaskResponse createTask(AlertMonitorTaskRequest request) {
        AlertMonitorTask task = new AlertMonitorTask();
        copyProperties(request, task);
        task = taskRepository.save(task);
        return convertToResponse(task);
    }
    
    /**
     * 更新预警监测任务
     */
    @Transactional
    public AlertMonitorTaskResponse updateTask(Long id, AlertMonitorTaskRequest request) {
        AlertMonitorTask task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("预警监测任务不存在: " + id));
        copyProperties(request, task);
        task = taskRepository.save(task);
        return convertToResponse(task);
    }
    
    /**
     * 删除预警监测任务
     */
    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
    
    /**
     * 获取预警监测任务详情
     */
    public AlertMonitorTaskResponse getTask(Long id) {
        AlertMonitorTask task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("预警监测任务不存在: " + id));
        return convertToResponse(task);
    }
    
    /**
     * 分页查询预警监测任务
     */
    public Page<AlertMonitorTaskResponse> listTasks(String status, String targetType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<AlertMonitorTask> tasks;
        
        if (status != null && !status.isEmpty()) {
            tasks = taskRepository.findByStatus(status, pageable);
        } else {
            tasks = taskRepository.findAll(pageable);
        }
        
        return tasks.map(this::convertToResponse);
    }
    
    /**
     * 启动任务
     */
    @Transactional
    public AlertMonitorTaskResponse startTask(Long id) {
        AlertMonitorTask task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("预警监测任务不存在: " + id));
        task.setStatus("running");
        task = taskRepository.save(task);
        return convertToResponse(task);
    }
    
    /**
     * 暂停任务
     */
    @Transactional
    public AlertMonitorTaskResponse pauseTask(Long id) {
        AlertMonitorTask task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("预警监测任务不存在: " + id));
        task.setStatus("paused");
        task = taskRepository.save(task);
        return convertToResponse(task);
    }
    
    /**
     * 获取预警排名数据
     */
    public AlertRankingResponse getRanking(String period, String targetType) {
        AlertRankingResponse response = new AlertRankingResponse();
        response.setPeriod(period);
        response.setTargetType(targetType);
        
        // 模拟数据 - 实际项目中应从数据库查询
        List<AlertRankingResponse.RankItem> items = generateMockRankingData(period, targetType);
        response.setItems(items);
        
        return response;
    }
    
    // ============= 私有方法 =============
    
    private void copyProperties(AlertMonitorTaskRequest request, AlertMonitorTask task) {
        task.setTaskName(request.getTaskName());
        task.setTargetType(request.getTargetType());
        if (request.getTargetIds() != null) {
            task.setTargetIds(String.join(",", request.getTargetIds()));
        }
        task.setFrequency(request.getFrequency());
        task.setMonitorTimeStart(request.getMonitorTimeStart());
        task.setMonitorTimeEnd(request.getMonitorTimeEnd());
        task.setNoDataThreshold(request.getNoDataThreshold());
        task.setNoDataUnit(request.getNoDataUnit());
        task.setSuccessRateThreshold(request.getSuccessRateThreshold());
        task.setDataChangeThreshold(request.getDataChangeThreshold());
        task.setStructureChangeDetect(request.getStructureChangeDetect());
        task.setRateLimitDetect(request.getRateLimitDetect());
        if (request.getNotifyChannels() != null) {
            task.setNotifyChannels(String.join(",", request.getNotifyChannels()));
        }
    }
    
    private AlertMonitorTaskResponse convertToResponse(AlertMonitorTask task) {
        AlertMonitorTaskResponse response = new AlertMonitorTaskResponse();
        response.setId(task.getId());
        response.setTaskName(task.getTaskName());
        response.setTargetType(task.getTargetType());
        if (task.getTargetIds() != null && !task.getTargetIds().isEmpty()) {
            response.setTargetIds(Arrays.asList(task.getTargetIds().split(",")));
        } else {
            response.setTargetIds(Collections.emptyList());
        }
        response.setFrequency(task.getFrequency());
        response.setMonitorTimeStart(task.getMonitorTimeStart());
        response.setMonitorTimeEnd(task.getMonitorTimeEnd());
        response.setNoDataThreshold(task.getNoDataThreshold());
        response.setNoDataUnit(task.getNoDataUnit());
        response.setSuccessRateThreshold(task.getSuccessRateThreshold());
        response.setDataChangeThreshold(task.getDataChangeThreshold());
        response.setStructureChangeDetect(task.getStructureChangeDetect());
        response.setRateLimitDetect(task.getRateLimitDetect());
        if (task.getNotifyChannels() != null && !task.getNotifyChannels().isEmpty()) {
            response.setNotifyChannels(Arrays.asList(task.getNotifyChannels().split(",")));
        } else {
            response.setNotifyChannels(Collections.emptyList());
        }
        response.setStatus(task.getStatus());
        response.setCreateTime(task.getCreateTime());
        response.setUpdateTime(task.getUpdateTime());
        response.setLastExecuteTime(task.getLastExecuteTime());
        return response;
    }
    
    private List<AlertRankingResponse.RankItem> generateMockRankingData(String period, String targetType) {
        // 生成模拟排名数据
        String[][] websiteData = {
            {"aljazeera.net", "18", "62", "187"},
            {"reuters.com", "12", "38", "112"},
            {"bbc.co.uk", "9", "45", "134"},
            {"cnn.com", "7", "22", "45"},
            {"nytimes.com", "4", "29", "65"}
        };
        
        String[][] socialData = {
            {"Al Jazeera TikTok", "15", "53", "162", "TikTok"},
            {"Reuters Twitter", "11", "34", "98", "Twitter"},
            {"BBC Facebook", "8", "41", "120", "Facebook"},
            {"CNN Instagram", "5", "19", "77", "Instagram"},
            {"NYT YouTube", "3", "13", "45", "YouTube"}
        };
        
        int index = period.equals("7d") ? 0 : period.equals("30d") ? 1 : 2;
        
        if ("website".equals(targetType)) {
            return Arrays.stream(websiteData)
                .map(data -> {
                    AlertRankingResponse.RankItem item = new AlertRankingResponse.RankItem();
                    item.setName(data[0]);
                    item.setCount(Integer.parseInt(data[index + 1]));
                    item.setType("website");
                    return item;
                })
                .collect(Collectors.toList());
        } else {
            return Arrays.stream(socialData)
                .map(data -> {
                    AlertRankingResponse.RankItem item = new AlertRankingResponse.RankItem();
                    item.setName(data[0]);
                    item.setCount(Integer.parseInt(data[index + 1]));
                    item.setType("social");
                    item.setPlatform(data[4]);
                    return item;
                })
                .collect(Collectors.toList());
        }
    }
}
