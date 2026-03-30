package com.datacollection.master.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务执行消息DTO
 * 用于Kafka传输
 */
@Data
public class TaskExecutionMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 执行ID（唯一标识一次执行）
     */
    private String executionId;
    
    /**
     * 任务ID
     */
    private String taskId;
    
    /**
     * 任务名称
     */
    private String taskName;
    
    /**
     * 任务类型
     */
    private String taskType;
    
    /**
     * 信源ID
     */
    private Long sourceId;
    
    /**
     * 信源名称
     */
    private String sourceName;
    
    /**
     * 平台
     */
    private String platform;
    
    /**
     * 信源URL列表
     */
    private List<String> sourceUrls;
    
    /**
     * 采集配置
     */
    private CollectConfig collectConfig;
    
    /**
     * 优先级
     */
    private String priority;
    
    /**
     * 调度时间
     */
    private LocalDateTime scheduleTime;
    
    /**
     * 超时时间（秒）
     */
    private Integer timeout;
    
    /**
     * 重试次数
     */
    private Integer retryCount;
    
    /**
     * 扩展参数（JSON格式）
     */
    private String extraParams;
    
    @Data
    public static class CollectConfig implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        /**
         * 是否采集图片
         */
        private Boolean collectImages;
        
        /**
         * 是否采集视频
         */
        private Boolean collectVideos;
        
        /**
         * 是否采集评论
         */
        private Boolean collectComments;
        
        /**
         * 采集层级
         */
        private Integer collectLevel;
        
        /**
         * 最大采集数量
         */
        private Integer maxCount;
    }
}
