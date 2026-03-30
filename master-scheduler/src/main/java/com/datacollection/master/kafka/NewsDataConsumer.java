package com.datacollection.master.kafka;

import com.datacollection.master.dto.CollectDataMessage;
import com.datacollection.master.entity.CollectData;
import com.datacollection.master.repository.CollectDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 新闻数据消费者
 * 负责从Kafka消费采集的新闻数据并写入数据库
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NewsDataConsumer {
    
    private final CollectDataRepository collectDataRepository;
    
    private static final String NEWS_DATA_TOPIC = "news-data";
    
    /**
     * 消费新闻数据消息
     * 
     * @param record Kafka消息记录
     * @param acknowledgment 手动确认
     */
    @KafkaListener(
        topics = NEWS_DATA_TOPIC,
        groupId = "master-scheduler-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeNewsData(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment) {
        try {
            log.info("[Master] 收到新闻数据消息: topic={}, partition={}, offset={}, key={}", 
                    record.topic(), record.partition(), record.offset(), record.key());
            
            // 将消息转换为CollectDataMessage
            Object value = record.value();
            if (value instanceof CollectDataMessage) {
                CollectDataMessage message = (CollectDataMessage) value;
                processNewsData(message);
            } else if (value instanceof java.util.Map) {
                // 处理JSON反序列化为Map的情况
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) value;
                CollectDataMessage message = convertMapToMessage(map);
                processNewsData(message);
            } else {
                log.warn("[Master] 未知的消息类型: {}", value.getClass().getName());
            }
            
            // 手动确认消息
            acknowledgment.acknowledge();
            
        } catch (Exception e) {
            log.error("[Master] 处理新闻数据消息失败: {}", e.getMessage(), e);
            // 出错也确认，避免阻塞后续消息
            acknowledgment.acknowledge();
        }
    }
    
    /**
     * 处理新闻数据
     */
    private void processNewsData(CollectDataMessage message) {
        // 检查是否已存在
        if (collectDataRepository.existsByDataId(message.getDataId())) {
            log.warn("[Master] 数据已存在，跳过: dataId={}", message.getDataId());
            return;
        }
        
        // 转换为实体并保存
        CollectData data = convertToEntity(message);
        collectDataRepository.save(data);
        
        log.info("[Master] 新闻数据已保存: dataId={}, title={}, source={}", 
                data.getDataId(), 
                truncate(data.getTitle(), 50), 
                data.getSource());
    }
    
    /**
     * 转换消息为实体
     */
    private CollectData convertToEntity(CollectDataMessage message) {
        CollectData data = new CollectData();
        data.setDataId(message.getDataId());
        data.setTaskId(message.getTaskId());
        data.setNodeId(message.getNodeId());
        data.setMainTab(message.getMainTab());
        data.setTitle(message.getTitle());
        data.setContent(message.getContent());
        data.setSource(message.getSource());
        data.setSourceType(message.getSourceType());
        data.setPlatform(message.getPlatform());
        data.setCountry(message.getCountry());
        data.setLanguage(message.getLanguage());
        data.setContentType(message.getContentType());
        data.setPublishTime(message.getPublishTime());
        data.setCollectTime(message.getCollectTime() != null ? 
                message.getCollectTime() : LocalDateTime.now());
        data.setUrl(message.getUrl());
        data.setAuthor(message.getAuthor());
        data.setImageCount(message.getImageCount());
        data.setVideoCount(message.getVideoCount());
        data.setWordCount(message.getWordCount());
        
        if (message.getInteraction() != null) {
            data.setLikes(message.getInteraction().getLikes());
            data.setComments(message.getInteraction().getComments());
            data.setShares(message.getInteraction().getShares());
            data.setViews(message.getInteraction().getViews());
        }
        
        data.setCategory(message.getCategory());
        data.setSentiment(message.getSentiment());
        
        if (message.getTags() != null) {
            data.setTags(String.join(",", message.getTags()));
        }
        
        return data;
    }
    
    /**
     * 从Map转换消息
     */
    private CollectDataMessage convertMapToMessage(java.util.Map<String, Object> map) {
        CollectDataMessage message = new CollectDataMessage();
        message.setDataId((String) map.get("dataId"));
        message.setTaskId((String) map.get("taskId"));
        message.setNodeId((String) map.get("nodeId"));
        message.setTitle((String) map.get("title"));
        message.setContent((String) map.get("content"));
        message.setSource((String) map.get("source"));
        message.setPlatform((String) map.get("platform"));
        message.setUrl((String) map.get("url"));
        message.setAuthor((String) map.get("author"));
        return message;
    }
    
    /**
     * 截断字符串
     */
    private String truncate(String str, int maxLength) {
        if (str == null) return null;
        return str.length() > maxLength ? str.substring(0, maxLength) + "..." : str;
    }
}
