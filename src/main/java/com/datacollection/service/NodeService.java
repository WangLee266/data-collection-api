package com.datacollection.service;

import com.datacollection.dto.*;
import com.datacollection.entity.Node;
import java.util.List;

/**
 * 节点服务接口
 */
public interface NodeService {
    
    /**
     * 添加节点
     */
    Node addNode(Node node);
    
    /**
     * 更新节点
     */
    Node updateNode(Long id, Node node);
    
    /**
     * 删除节点
     */
    void deleteNode(Long id);
    
    /**
     * 获取节点详情
     */
    Node getNodeById(Long id);
    
    /**
     * 获取所有节点
     */
    List<Node> getAllNodes();
    
    /**
     * 获取在线节点
     */
    List<Node> getOnlineNodes();
    
    /**
     * 重启节点
     */
    void restartNode(String nodeId);
    
    /**
     * 关闭节点
     */
    void shutdownNode(String nodeId);
    
    /**
     * 更新节点心跳
     */
    void updateHeartbeat(String nodeId);
    
    /**
     * 获取节点统计
     */
    NodeStatsResponse getNodeStats();
}
