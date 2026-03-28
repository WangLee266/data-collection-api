package com.datacollection.repository;

import com.datacollection.entity.Node;
import com.datacollection.enums.NodeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    
    Optional<Node> findByNodeId(String nodeId);
    
    List<Node> findByStatus(NodeStatus status);
    
    @Query("SELECT COUNT(n) FROM Node n WHERE n.status = 'NORMAL'")
    Integer countOnline();
    
    @Query("SELECT COUNT(n) FROM Node n WHERE n.status = 'ERROR'")
    Integer countOffline();
    
    @Query("SELECT COUNT(n) FROM Node n WHERE n.status = 'WARNING'")
    Integer countWarning();
    
    @Query("SELECT AVG(n.cpu) FROM Node n WHERE n.status = 'NORMAL'")
    Double avgCpu();
    
    @Query("SELECT AVG(n.memory) FROM Node n WHERE n.status = 'NORMAL'")
    Double avgMemory();
    
    @Query("SELECT SUM(n.taskCount) FROM Node n")
    Integer sumTaskCount();
    
    @Query("SELECT n FROM Node n WHERE n.lastHeartbeat < :threshold AND n.status != 'ERROR'")
    List<Node> findOfflineNodes(LocalDateTime threshold);
}
