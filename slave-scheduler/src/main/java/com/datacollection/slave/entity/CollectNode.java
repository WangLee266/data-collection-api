package com.datacollection.slave.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 采集节点实体
 */
@Data
@Entity
@Table(name = "collect_node")
public class CollectNode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "node_id", unique = true, length = 30)
    private String nodeId;
    
    @Column(length = 50)
    private String ip;
    
    @Column(length = 50)
    private String region;
    
    @Column(length = 50)
    private String isp;
    
    @Column(length = 20)
    private String type;
    
    @Column(length = 20)
    private String status;
    
    private Integer cpu;
    
    private Integer memory;
    
    private Integer disk;
    
    @Column(name = "task_count")
    private Integer taskCount;
    
    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;
    
    /**
     * 节点支持的采集类型（逗号分隔，如：website,social,thinktank）
     */
    @Column(name = "supported_types", length = 100)
    private String supportedTypes;
    
    /**
     * Flask服务端口
     */
    @Column(name = "api_port")
    private Integer apiPort;
    
    /**
     * Flask服务URL
     */
    @Column(name = "api_url", length = 255)
    private String apiUrl;
    
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
