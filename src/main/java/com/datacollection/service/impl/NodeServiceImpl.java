package com.datacollection.service.impl;

import com.datacollection.dto.*;
import com.datacollection.entity.Node;
import com.datacollection.enums.NodeStatus;
import com.datacollection.repository.NodeRepository;
import com.datacollection.service.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NodeServiceImpl implements NodeService {
    
    private final NodeRepository nodeRepository;
    
    @Override
    @Transactional
    public Node addNode(Node node) {
        node.setNodeId("NODE-" + String.format("%03d", nodeRepository.count() + 1));
        node.setStatus(NodeStatus.NORMAL);
        return nodeRepository.save(node);
    }
    
    @Override
    @Transactional
    public Node updateNode(Long id, Node node) {
        Node existing = nodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("节点不存在"));
        node.setId(id);
        return nodeRepository.save(node);
    }
    
    @Override
    @Transactional
    public void deleteNode(Long id) {
        nodeRepository.deleteById(id);
    }
    
    @Override
    public Node getNodeById(Long id) {
        return nodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("节点不存在"));
    }
    
    @Override
    public List<Node> getAllNodes() {
        return nodeRepository.findAll();
    }
    
    @Override
    public List<Node> getOnlineNodes() {
        return nodeRepository.findByStatus(NodeStatus.NORMAL);
    }
    
    @Override
    @Transactional
    public void restartNode(String nodeId) {
        Node node = nodeRepository.findByNodeId(nodeId)
                .orElseThrow(() -> new RuntimeException("节点不存在"));
        node.setStatus(NodeStatus.NORMAL);
        node.setLastHeartbeat(LocalDateTime.now());
        nodeRepository.save(node);
    }
    
    @Override
    @Transactional
    public void shutdownNode(String nodeId) {
        Node node = nodeRepository.findByNodeId(nodeId)
                .orElseThrow(() -> new RuntimeException("节点不存在"));
        node.setStatus(NodeStatus.ERROR);
        nodeRepository.save(node);
    }
    
    @Override
    @Transactional
    public void updateHeartbeat(String nodeId) {
        Node node = nodeRepository.findByNodeId(nodeId)
                .orElseThrow(() -> new RuntimeException("节点不存在"));
        node.setLastHeartbeat(LocalDateTime.now());
        node.setStatus(NodeStatus.NORMAL);
        nodeRepository.save(node);
    }
    
    @Override
    public NodeStatsResponse getNodeStats() {
        NodeStatsResponse stats = new NodeStatsResponse();
        stats.setTotalNodes(nodeRepository.findAll().size());
        stats.setOnlineNodes(nodeRepository.countOnline());
        stats.setOfflineNodes(nodeRepository.countOffline());
        stats.setErrorNodes(nodeRepository.countWarning());
        stats.setAvgCpu(nodeRepository.avgCpu());
        stats.setAvgMemory(nodeRepository.avgMemory());
        stats.setRunningTasks(nodeRepository.sumTaskCount());
        return stats;
    }
}
