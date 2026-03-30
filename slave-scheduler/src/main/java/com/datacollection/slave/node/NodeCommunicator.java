package com.datacollection.slave.node;

import com.datacollection.slave.dto.CollectTaskRequest;
import com.datacollection.slave.dto.CollectTaskResponse;
import com.datacollection.slave.entity.CollectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 节点通信服务
 * 负责与Python执行引擎通信
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NodeCommunicator {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Value("${collector.timeout:300}")
    private int defaultTimeout;
    
    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    
    /**
     * 向指定节点发送采集任务指令
     * 
     * @param node 目标节点
     * @param request 采集任务请求
     * @return 采集响应
     */
    public CollectTaskResponse sendCollectTask(CollectNode node, CollectTaskRequest request) {
        String url = buildCollectUrl(node);
        
        log.info("[Slave] 发送采集任务到节点: nodeId={}, url={}, executionId={}", 
                node.getNodeId(), url, request.getExecutionId());
        
        try {
            String requestBody = objectMapper.writeValueAsString(request);
            
            Request httpRequest = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(
                            requestBody, 
                            MediaType.parse("application/json; charset=utf-8")))
                    .build();
            
            try (Response response = httpClient.newCall(httpRequest).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    CollectTaskResponse result = objectMapper.readValue(
                            responseBody, CollectTaskResponse.class);
                    
                    log.info("[Slave] 采集任务完成: executionId={}, status={}, dataCount={}", 
                            result.getExecutionId(), 
                            result.getStatus(), 
                            result.getDataCount());
                    
                    return result;
                } else {
                    log.error("[Slave] 采集任务失败: nodeId={}, httpCode={}, message={}", 
                            node.getNodeId(), 
                            response.code(), 
                            response.message());
                    
                    return createErrorResponse(request.getExecutionId(), 
                            "HTTP错误: " + response.code() + " " + response.message());
                }
            }
            
        } catch (IOException e) {
            log.error("[Slave] 发送采集任务异常: nodeId={}, error={}", 
                    node.getNodeId(), e.getMessage(), e);
            return createErrorResponse(request.getExecutionId(), 
                    "通信异常: " + e.getMessage());
        }
    }
    
    /**
     * 检查节点健康状态
     * 
     * @param node 目标节点
     * @return 是否健康
     */
    public boolean checkNodeHealth(CollectNode node) {
        String url = buildHealthUrl(node);
        
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                boolean healthy = response.isSuccessful();
                log.debug("[Slave] 节点健康检查: nodeId={}, healthy={}", 
                        node.getNodeId(), healthy);
                return healthy;
            }
            
        } catch (IOException e) {
            log.warn("[Slave] 节点健康检查失败: nodeId={}, error={}", 
                    node.getNodeId(), e.getMessage());
            return false;
        }
    }
    
    /**
     * 构建采集接口URL
     */
    private String buildCollectUrl(CollectNode node) {
        String baseUrl = node.getApiUrl();
        if (baseUrl == null || baseUrl.isEmpty()) {
            int port = node.getApiPort() != null ? node.getApiPort() : 5000;
            baseUrl = "http://" + node.getIp() + ":" + port;
        }
        return baseUrl + "/api/collect";
    }
    
    /**
     * 构建健康检查URL
     */
    private String buildHealthUrl(CollectNode node) {
        String baseUrl = node.getApiUrl();
        if (baseUrl == null || baseUrl.isEmpty()) {
            int port = node.getApiPort() != null ? node.getApiPort() : 5000;
            baseUrl = "http://" + node.getIp() + ":" + port;
        }
        return baseUrl + "/api/health";
    }
    
    /**
     * 创建错误响应
     */
    private CollectTaskResponse createErrorResponse(String executionId, String errorMessage) {
        CollectTaskResponse response = new CollectTaskResponse();
        response.setExecutionId(executionId);
        response.setStatus("FAILED");
        response.setErrorMessage(errorMessage);
        return response;
    }
}
