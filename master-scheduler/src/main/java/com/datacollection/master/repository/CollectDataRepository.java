package com.datacollection.master.repository;

import com.datacollection.master.entity.CollectData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 采集数据Repository
 */
@Repository
public interface CollectDataRepository extends JpaRepository<CollectData, Long> {
    
    /**
     * 按数据ID查询
     */
    CollectData findByDataId(String dataId);
    
    /**
     * 检查数据ID是否存在
     */
    boolean existsByDataId(String dataId);
    
    /**
     * 按任务ID查询
     */
    List<CollectData> findByTaskId(String taskId);
    
    /**
     * 按采集时间范围查询
     */
    @Query("SELECT d FROM CollectData d WHERE d.collectTime BETWEEN :start AND :end ORDER BY d.collectTime DESC")
    List<CollectData> findByCollectTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    /**
     * 统计指定时间范围内的数据量
     */
    @Query("SELECT COUNT(d) FROM CollectData d WHERE d.collectTime >= :since")
    long countSince(@Param("since") LocalDateTime since);
}
