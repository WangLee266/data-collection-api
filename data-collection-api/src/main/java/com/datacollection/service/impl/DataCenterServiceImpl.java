package com.datacollection.service.impl;

import com.datacollection.dto.*;
import com.datacollection.entity.CollectData;
import com.datacollection.repository.CollectDataRepository;
import com.datacollection.service.DataCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataCenterServiceImpl implements DataCenterService {
    
    private final CollectDataRepository collectDataRepository;
    
    @Override
    public List<CollectData> searchData(String mainTab, String sourceType, String platform, 
                                         String keyword, String country, String language, String category) {
        return collectDataRepository.searchData(mainTab, sourceType, platform, keyword, country, language, category);
    }
    
    @Override
    public CollectData getDataById(Long id) {
        return collectDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("数据不存在"));
    }
    
    @Override
    public byte[] exportData(List<Long> ids, String format) {
        // TODO: 实现数据导出逻辑
        List<CollectData> dataList = collectDataRepository.findAllById(ids);
        StringBuilder sb = new StringBuilder();
        sb.append("ID,标题,来源,平台,发布时间,采集时间\n");
        for (CollectData data : dataList) {
            sb.append(data.getId()).append(",");
            sb.append(data.getTitle() != null ? data.getTitle().replace(",", "，") : "").append(",");
            sb.append(data.getSource() != null ? data.getSource().replace(",", "，") : "").append(",");
            sb.append(data.getPlatform() != null ? data.getPlatform() : "").append(",");
            sb.append(data.getPublishTime() != null ? data.getPublishTime().toString() : "").append(",");
            sb.append(data.getCollectTime() != null ? data.getCollectTime().toString() : "").append("\n");
        }
        return sb.toString().getBytes();
    }
    
    @Override
    public Long countTotal() {
        return collectDataRepository.countTotal();
    }
    
    @Override
    public Long countToday() {
        return collectDataRepository.countToday(LocalDate.now().atStartOfDay());
    }
}
