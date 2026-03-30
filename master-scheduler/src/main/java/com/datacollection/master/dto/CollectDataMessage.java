package com.datacollection.master.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 采集数据消息DTO
 * 用于Kafka传输采集结果数据
 */
@Data
public class CollectDataMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 数据ID（唯一标识）
     */
    private String dataId;
    
    /**
     * 任务ID
     */
    private String taskId;
    
    /**
     * 执行ID
     */
    private String executionId;
    
    /**
     * 节点ID
     */
    private String nodeId;
    
    /**
     * 主分类
     */
    private String mainTab;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 内容
     */
    private String content;
    
    /**
     * 来源
     */
    private String source;
    
    /**
     * 信源类型
     */
    private String sourceType;
    
    /**
     * 平台
     */
    private String platform;
    
    /**
     * 国家
     */
    private String country;
    
    /**
     * 语言
     */
    private String language;
    
    /**
     * 内容类型
     */
    private String contentType;
    
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    
    /**
     * 采集时间
     */
    private LocalDateTime collectTime;
    
    /**
     * 原文URL
     */
    private String url;
    
    /**
     * 作者
     */
    private String author;
    
    /**
     * 图片URL列表
     */
    private List<String> imageUrls;
    
    /**
     * 视频URL列表
     */
    private List<String> videoUrls;
    
    /**
     * 图片数量
     */
    private Integer imageCount;
    
    /**
     * 视频数量
     */
    private Integer videoCount;
    
    /**
     * 字数
     */
    private Integer wordCount;
    
    /**
     * 互动数据
     */
    private InteractionData interaction;
    
    /**
     * 分类
     */
    private String category;
    
    /**
     * 情感倾向
     */
    private String sentiment;
    
    /**
     * 标签列表
     */
    private List<String> tags;
    
    /**
     * 原始数据（JSON格式，用于存储原始采集数据）
     */
    private String rawData;
    
    @Data
    public static class InteractionData implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        private Long likes;
        private Long comments;
        private Long shares;
        private Long views;
    }
}
