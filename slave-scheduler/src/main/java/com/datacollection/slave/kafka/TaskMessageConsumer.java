package com.datacollection.slave.kafka;

import com.datacollection.slave.dto.CollectDataMessage;
import com.datacollection.slave.dto.CollectTaskRequest;
import com.datacollection.slave.dto.CollectTaskResponse;
import com.datacollection.slave.dto.TaskExecutionMessage;
import com.datacollection.slave.entity.CollectNode;
import com.datacollection.slave.entity.TaskExecution;
import com.datacollection.slave.kafka.DataMessageProducer;
import com.datacollection.slave.node.NodeCommunicator;
import com.datacollection.slave.node.NodeSelector;
import com.datacollection.slave.repository.TaskExecutionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 任务消息消费者
 * 从Kafka消费任务并分发给采集节点执行
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskMessageConsumer {
    
    private final NodeSelector nodeSelector;
    private final NodeCommunicator nodeCommunicator;
    private final DataMessageProducer dataMessageProducer;
    private final TaskExecutionRepository executionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private static final String TASK_QUEUE_TOPIC = "task-queue";
    
    /**
     * 消费任务消息
     * 
     * @param record Kafka消息记录
     * @param acknowledgment 手动确认
     */
    @KafkaListener(
        topics = TASK_QUEUE_TOPIC,
        groupId = "slave-scheduler-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeTaskMessage(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment) {
        log.info("[Slave] 收到任务消息: topic={}, partition={}, offset={}, key={}", 
                record.topic(), record.partition(), record.offset(), record.key());
        
        try {
            // 解析任务消息
            TaskExecutionMessage message = parseMessage(record.value());
            if (message == null) {
                log.error("[Slave] 解析任务消息失败");
                acknowledgment.acknowledge();
                return;
            }
            
            // 记录Kafka信息
            message.setKafkaPartition(record.partition());
            message.setKafkaOffset(record.offset());
            
            // 处理任务
            processTask(message);
            
            // 手动确认消息
            acknowledgment.acknowledge();
            
        } catch (Exception e) {
            log.error("[Slave] 处理任务消息失败: {}", e.getMessage(), e);
            acknowledgment.acknowledge();
        }
    }
    
    /**
     * 解析消息
     */
    @SuppressWarnings("unchecked")
    private TaskExecutionMessage parseMessage(Object value) {
        try {
            if (value instanceof TaskExecutionMessage) {
                return (TaskExecutionMessage) value;
            } else if (value instanceof Map) {
                return objectMapper.convertValue(value, TaskExecutionMessage.class);
            }
            return null;
        } catch (Exception e) {
            log.error("[Slave] 解析消息异常: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 处理任务
     */
    private void processTask(TaskExecutionMessage message) {
        String executionId = message.getExecutionId();
        String taskId = message.getTaskId();
        
        log.info("[Slave] 开始处理任务: executionId={}, taskId={}, type={}", 
                executionId, taskId, message.getTaskType());
        
        // 创建执行记录
        TaskExecution execution = createExecution(message);
        
        try {
            // 选择节点
            Optional<CollectNode> nodeOpt = nodeSelector.selectNode(message.getTaskType());
            if (nodeOpt.isEmpty()) {
                log.error("[Slave] 没有可用节点执行任务: taskId={}", taskId);
                updateExecutionFailed(execution, "没有可用节点");
                return;
            }
            
            CollectNode node = nodeOpt.get();
            execution.setNodeId(node.getNodeId());
            execution.setStatus("RUNNING");
            execution.setStartTime(LocalDateTime.now());
            executionRepository.save(execution);
            
            // 更新节点任务数
            nodeSelector.incrementTaskCount(node.getNodeId());
            
            // 构建采集请求
            CollectTaskRequest request = buildCollectRequest(message);
            
            // 发送到采集引擎
            CollectTaskResponse response = nodeCommunicator.sendCollectTask(node, request);
            
            // 处理响应
            if ("SUCCESS".equals(response.getStatus()) || "COMPLETED".equals(response.getStatus())) {
                // 发送采集数据到Kafka
                if (response.getDataList() != null && !response.getDataList().isEmpty()) {
                    sendCollectedData(message, node, response.getDataList());
                }
                
                execution.setStatus("COMPLETED");
                execution.setDataCount(response.getDataCount());
                execution.setMessage("采集完成");
                log.info("[Slave] 任务执行成功: executionId={}, dataCount={}", 
                        executionId, response.getDataCount());
            } else {
                execution.setStatus("FAILED");
                execution.setMessage(response.getErrorMessage());
                log.error("[Slave] 任务执行失败: executionId={}, error={}", 
                        executionId, response.getErrorMessage());
            }
            
            execution.setEndTime(LocalDateTime.now());
            executionRepository.save(execution);
            
            // 更新节点任务数
            nodeSelector.decrementTaskCount(node.getNodeId());
            
        } catch (Exception e) {
            log.error("[Slave] 处理任务异常: executionId={}, error={}", 
                    executionId, e.getMessage(), e);
            updateExecutionFailed(execution, e.getMessage());
        }
    }
    
    /**
     * 创建执行记录
     */
    private TaskExecution createExecution(TaskExecutionMessage message) {
        TaskExecution execution = new TaskExecution();
        execution.setExecutionId(message.getExecutionId());
        execution.setTaskId(message.getTaskId());
        execution.setStatus("PENDING");
        execution.setRetryCount(message.getRetryCount() != null ? message.getRetryCount() : 0);
        return executionRepository.save(execution);
    }
    
    /**
     * 更新执行失败状态
     */
    private void updateExecutionFailed(TaskExecution execution, String errorMessage) {
        execution.setStatus("FAILED");
        execution.setMessage(errorMessage);
        execution.setEndTime(LocalDateTime.now());
        executionRepository.save(execution);
    }
    
    /**
     * 构建采集请求
     */
    private CollectTaskRequest buildCollectRequest(TaskExecutionMessage message) {
        CollectTaskRequest request = new CollectTaskRequest();
        request.setExecutionId(message.getExecutionId());
        request.setTaskId(message.getTaskId());
        request.setTaskType(message.getTaskType());
        request.setUrls(message.getSourceUrls());
        
        CollectTaskRequest.CollectConfig config = new CollectTaskRequest.CollectConfig();
        if (message.getCollectConfig() != null) {
            config.setCollectImages(message.getCollectConfig().getCollectImages());
            config.setCollectVideos(message.getCollectConfig().getCollectVideos());
            config.setCollectComments(message.getCollectConfig().getCollectComments());
            config.setMaxCount(message.getCollectConfig().getMaxCount());
        }
        config.setTimeout(message.getTimeout() != null ? message.getTimeout() : 300);
        request.setConfig(config);
        
        return request;
    }
    
    /**
     * 发送采集数据到Kafka
     */
    @SuppressWarnings("unchecked")
    private void sendCollectedData(TaskExecutionMessage taskMessage, CollectNode node, 
                                  List<Map<String, Object>> dataList) {
        
        for (Map<String, Object> data : dataList) {
            try {
                CollectDataMessage dataMessage = new CollectDataMessage();
                dataMessage.setDataId(UUID.randomUUID().toString());
                dataMessage.setTaskId(taskMessage.getTaskId());
                dataMessage.setExecutionId(taskMessage.getExecutionId());
                dataMessage.setNodeId(node.getNodeId());
                
                // 设置数据字段
                dataMessage.setTitle((String) data.get("title"));
                dataMessage.setContent((String) data.get("content"));
                dataMessage.setSource((String) data.get("source"));
                dataMessage.setPlatform(taskMessage.getPlatform());
                dataMessage.setUrl((String) data.get("url"));
                dataMessage.setAuthor((String) data.get("author"));
                dataMessage.setMainTab("website".equals(taskMessage.getTaskType()) ? "news" : "social");
                dataMessage.setContentType((String) data.get("contentType"));
                
                // 处理图片URLs
                Object imageUrls = data.get("imageUrls");
                if (imageUrls instanceof List) {
                    dataMessage.setImageUrls((List<String>) imageUrls);
                    dataMessage.setImageCount(((List<?>) imageUrls).size());
                }
                
                // 处理互动数据
                Object interaction = data.get("interaction");
                if (interaction instanceof Map) {
                    CollectDataMessage.InteractionData interactionData = new CollectDataMessage.InteractionData();
                    Map<String, Object> interactionMap = (Map<String, Object>) interaction;
                    interactionData.setLikes(getLong(interactionMap, "likes"));
                    interactionData.setComments(getLong(interactionMap, "comments"));
                    interactionData.setShares(getLong(interactionMap, "shares"));
                    interactionData.setViews(getLong(interactionMap, "views"));
                    dataMessage.setInteraction(interactionData);
                }
                
                dataMessage.setCollectTime(LocalDateTime.now());
                
                // 发送到Kafka
                dataMessageProducer.sendDataMessage(dataMessage);
                
            } catch (Exception e) {
                log.error("[Slave] 处理采集数据异常: {}", e.getMessage());
            }
        }
    }
    
    private Long getLong(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return 0L;
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return 0L;
    }
    
    // 用于存储Kafka信息的临时字段
    private Integer kafkaPartition;
    private Long kafkaOffset;
    
    public void setKafkaPartition(Integer kafkaPartition) {
        this.kafkaPartition = kafkaPartition;
    }
    
    public void setKafkaOffset(Long kafkaOffset) {
        this.kafkaOffset = kafkaOffset;
    }
}
