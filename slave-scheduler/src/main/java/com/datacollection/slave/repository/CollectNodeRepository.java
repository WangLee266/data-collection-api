package com.datacollection.slave.repository;

import com.datacollection.slave.entity.CollectNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 采集节点Repository
 */
@Repository
public interface CollectNodeRepository extends JpaRepository<CollectNode, Long> {
    
    /**
     * 按节点ID查询
     */
    CollectNode findByNodeId(String nodeId);
    
    /**
     * 按状态查询节点
     */
    List<CollectNode> findByStatus(String status);
    
    /**
     * 查询所有在线节点
     */
    @Query("SELECT n FROM CollectNode n WHERE n.status = 'NORMAL' OR n.status = 'WARNING'")
    List<CollectNode> findOnlineNodes();
    
    /**
     * 查询支持指定类型的节点
     */
    @Query("SELECT n FROM CollectNode n WHERE (n.status = 'NORMAL' OR n.status = 'WARNING') AND n.supportedTypes LIKE %:type%")
    List<CollectNode> findBySupportedType(@Param("type") String type);
    
    /**
     * 查询任务数最少的节点（负载均衡）
     */
    @Query("SELECT n FROM CollectNode n WHERE (n.status = 'NORMAL' OR n.status = 'WARNING') AND n.supportedTypes LIKE %:type% ORDER BY n.taskCount ASC, n.cpu ASC")
    List<CollectNode> findLeastBusyNodes(@Param("type") String type);
    
    /**
     * 统计在线节点数
     */
    @Query("SELECT COUNT(n) FROM CollectNode n WHERE n.status = 'NORMAL'")
    long countOnlineNodes();
}
