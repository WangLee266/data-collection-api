package com.datacollection.slave.kafka;

import com.datacollection.slave.dto.CollectDataMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * 数据消息生产者
 * 负责将采集数据推送到Kafka新闻数据Topic
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataMessageProducer {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    private static final String NEWS_DATA_TOPIC = "news-data";
    
    /**
     * 发送采集数据消息到Kafka
     * 
     * @param message 采集数据消息
     * @return 发送结果
     */
    public CompletableFuture<SendResult<String, Object>> sendDataMessage(CollectDataMessage message) {
        String key = message.getDataId();
        
        log.info("[Slave] 发送数据消息到Kafka: dataId={}, topic={}", 
                message.getDataId(), NEWS_DATA_TOPIC);
        
        return kafkaTemplate.send(NEWS_DATA_TOPIC, key, message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("[Slave] 发送数据消息失败: dataId={}, error={}", 
                                message.getDataId(), ex.getMessage(), ex);
                    } else {
                        log.info("[Slave] 发送数据消息成功: dataId={}, partition={}, offset={}", 
                                message.getDataId(), 
                                result.getRecordMetadata().partition(), 
                                result.getRecordMetadata().offset());
                    }
                });
    }
    
    /**
     * 批量发送采集数据消息
     * 
     * @param messages 采集数据消息列表
     */
    public void sendDataMessages(java.util.List<CollectDataMessage> messages) {
        for (CollectDataMessage message : messages) {
            sendDataMessage(message);
        }
        log.info("[Slave] 批量发送数据消息完成: count={}", messages.size());
    }
}
