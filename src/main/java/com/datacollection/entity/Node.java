package com.datacollection.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 采集节点实体
 */
@Data
@Entity
@Table(name = "collect_node")
public class Node {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "node_id", unique = true, nullable = false, length = 30)
    private String nodeId;
    
    @Column(nullable = false, length = 50)
    private String ip;
    
    @Column(length = 50)
    private String region;
    
    @Column(length = 50)
    private String isp;
    
    @Column(length = 20)
    private String type;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private com.datacollection.enums.NodeStatus status = com.datacollection.enums.NodeStatus.NORMAL;
    
    @Column
    private Integer cpu;
    
    @Column
    private Integer memory;
    
    @Column
    private Integer disk;
    
    @Column(name = "task_count")
    private Integer taskCount = 0;
    
    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;
    
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (nodeId == null) {
            nodeId = "NODE-" + String.format("%03d", id);
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
