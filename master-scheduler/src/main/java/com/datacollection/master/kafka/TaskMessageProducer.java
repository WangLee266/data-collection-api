package com.datacollection.master.kafka;

import com.datacollection.master.dto.TaskExecutionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * 任务消息生产者
 * 负责将任务执行消息推送到Kafka任务队列
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskMessageProducer {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    private static final String TASK_QUEUE_TOPIC = "task-queue";
    
    /**
     * 发送任务执行消息到Kafka
     * 
     * @param message 任务执行消息
     * @return 发送结果
     */
    public CompletableFuture<SendResult<String, Object>> sendTaskMessage(TaskExecutionMessage message) {
        String key = message.getTaskId();
        
        log.info("[Master] 发送任务消息到Kafka: taskId={}, executionId={}, topic={}", 
                message.getTaskId(), message.getExecutionId(), TASK_QUEUE_TOPIC);
        
        return kafkaTemplate.send(TASK_QUEUE_TOPIC, key, message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("[Master] 发送任务消息失败: taskId={}, error={}", 
                                message.getTaskId(), ex.getMessage(), ex);
                    } else {
                        log.info("[Master] 发送任务消息成功: taskId={}, partition={}, offset={}", 
                                message.getTaskId(), 
                                result.getRecordMetadata().partition(), 
                                result.getRecordMetadata().offset());
                    }
                });
    }
    
    /**
     * 发送任务执行消息到指定分区
     * 
     * @param message 任务执行消息
     * @param partition 分区号
     * @return 发送结果
     */
    public CompletableFuture<SendResult<String, Object>> sendTaskMessage(
            TaskExecutionMessage message, int partition) {
        
        log.info("[Master] 发送任务消息到指定分区: taskId={}, partition={}", 
                message.getTaskId(), partition);
        
        return kafkaTemplate.send(TASK_QUEUE_TOPIC, partition, message.getTaskId(), message);
    }
}
