package com.datacollection.slave.node;

import com.datacollection.slave.entity.CollectNode;
import com.datacollection.slave.repository.CollectNodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 节点选择服务
 * 负责从服务器集群中选择合适的节点执行采集任务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NodeSelector {
    
    private final CollectNodeRepository nodeRepository;
    
    /**
     * 选择最适合执行指定类型任务的节点
     * 
     * @param taskType 任务类型（website, social, thinktank）
     * @return 选中的节点，如果没有可用节点则返回空
     */
    public Optional<CollectNode> selectNode(String taskType) {
        // 转换任务类型
        String type = convertTaskType(taskType);
        
        // 查询支持该类型且负载最低的节点
        List<CollectNode> nodes = nodeRepository.findLeastBusyNodes(type);
        
        if (nodes.isEmpty()) {
            log.warn("[Slave] 没有可用的节点支持任务类型: {}", taskType);
            return Optional.empty();
        }
        
        // 选择负载最低的节点
        CollectNode selectedNode = nodes.get(0);
        
        log.info("[Slave] 选择节点: nodeId={}, ip={}, type={}, currentTasks={}", 
                selectedNode.getNodeId(), 
                selectedNode.getIp(), 
                taskType,
                selectedNode.getTaskCount());
        
        return Optional.of(selectedNode);
    }
    
    /**
     * 转换任务类型到节点支持的类型标识
     */
    private String convertTaskType(String taskType) {
        switch (taskType.toLowerCase()) {
            case "website":
            case "thinktank":
                return "website";
            case "social":
                return "social";
            default:
                return "website";
        }
    }
    
    /**
     * 获取所有在线节点
     */
    public List<CollectNode> getOnlineNodes() {
        return nodeRepository.findOnlineNodes();
    }
    
    /**
     * 获取指定节点
     */
    public Optional<CollectNode> getNode(String nodeId) {
        CollectNode node = nodeRepository.findByNodeId(nodeId);
        return Optional.ofNullable(node);
    }
    
    /**
     * 更新节点任务数
     */
    public void incrementTaskCount(String nodeId) {
        CollectNode node = nodeRepository.findByNodeId(nodeId);
        if (node != null) {
            node.setTaskCount(node.getTaskCount() == null ? 1 : node.getTaskCount() + 1);
            nodeRepository.save(node);
        }
    }
    
    /**
     * 减少节点任务数
     */
    public void decrementTaskCount(String nodeId) {
        CollectNode node = nodeRepository.findByNodeId(nodeId);
        if (node != null && node.getTaskCount() != null && node.getTaskCount() > 0) {
            node.setTaskCount(node.getTaskCount() - 1);
            nodeRepository.save(node);
        }
    }
    
    /**
     * 统计在线节点数
     */
    public long countOnlineNodes() {
        return nodeRepository.countOnlineNodes();
    }
}
