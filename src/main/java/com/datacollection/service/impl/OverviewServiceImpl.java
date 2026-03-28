package com.datacollection.service.impl;

import com.datacollection.dto.*;
import com.datacollection.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OverviewServiceImpl implements OverviewService {
    
    private final WebsiteRepository websiteRepository;
    private final SocialAccountRepository socialAccountRepository;
    private final PersonRepository personRepository;
    private final OrganizationRepository organizationRepository;
    private final CollectDataRepository collectDataRepository;
    private final TaskRepository taskRepository;
    private final NodeRepository nodeRepository;
    
    @Override
    public OverviewStatsResponse getOverviewStats() {
        OverviewStatsResponse response = new OverviewStatsResponse();
        
        // 统计卡片
        OverviewStatsResponse.StatCards statCards = new OverviewStatsResponse.StatCards();
        statCards.setTotalSources(websiteRepository.countActive() + socialAccountRepository.countActive());
        statCards.setValidSources(statCards.getTotalSources()); // 简化处理
        statCards.setTotalData("12.8亿");
        statCards.setTodayCollected(collectDataRepository.countToday(LocalDate.now().atStartOfDay()));
        statCards.setOnlineNodes(nodeRepository.countOnline().longValue());
        statCards.setTotalNodes((long) nodeRepository.findAll().size());
        statCards.setRunningTasks(taskRepository.countByStatus(com.datacollection.enums.TaskStatus.RUNNING));
        statCards.setTodayExecutions(taskRepository.countCreatedToday(LocalDate.now().atStartOfDay()));
        response.setStatCards(statCards);
        
        // 平台分布
        List<Object[]> platformCounts = socialAccountRepository.countByPlatform();
        List<OverviewStatsResponse.PlatformData> platformData = new ArrayList<>();
        Map<String, String> platformColors = Map.of(
            "X", "rgba(24,144,255,1)",
            "Facebook", "rgba(0,212,255,1)",
            "Instagram", "rgba(149,97,226,1)",
            "YouTube", "rgba(255,77,79,1)",
            "TikTok", "rgba(250,173,20,1)",
            "LinkedIn", "rgba(82,196,26,1)"
        );
        for (Object[] row : platformCounts) {
            OverviewStatsResponse.PlatformData pd = new OverviewStatsResponse.PlatformData();
            pd.setName((String) row[0]);
            pd.setCount((Long) row[1]);
            pd.setColor(platformColors.getOrDefault(pd.getName(), "rgba(120,160,210,0.8)"));
            platformData.add(pd);
        }
        response.setPlatformData(platformData);
        
        // 信源分类统计
        List<OverviewStatsResponse.SourceCategoryData> sourceCategoryData = new ArrayList<>();
        OverviewStatsResponse.SourceCategoryData websiteCat = new OverviewStatsResponse.SourceCategoryData();
        websiteCat.setLabel("媒体网站");
        websiteCat.setTotal(websiteRepository.countActive());
        websiteCat.setValid(websiteCat.getTotal());
        websiteCat.setColor("rgba(24,144,255,1)");
        sourceCategoryData.add(websiteCat);
        
        OverviewStatsResponse.SourceCategoryData socialCat = new OverviewStatsResponse.SourceCategoryData();
        socialCat.setLabel("社交平台");
        socialCat.setTotal(socialAccountRepository.countActive());
        socialCat.setValid(socialCat.getTotal());
        socialCat.setColor("rgba(149,97,226,1)");
        sourceCategoryData.add(socialCat);
        
        response.setSourceCategoryData(sourceCategoryData);
        
        // 趋势数据（示例数据）
        List<OverviewStatsResponse.TrendData> trendData = generateTrendData();
        response.setTrendData(trendData);
        
        return response;
    }
    
    private List<OverviewStatsResponse.TrendData> generateTrendData() {
        List<OverviewStatsResponse.TrendData> list = new ArrayList<>();
        String[] days = {"06-25", "06-26", "06-27", "06-28", "06-29", "06-30", "07-01"};
        Random random = new Random();
        for (String day : days) {
            OverviewStatsResponse.TrendData data = new OverviewStatsResponse.TrendData();
            data.setDay(day);
            data.setWebsite(150 + random.nextInt(200));
            data.setThinkTank(30 + random.nextInt(50));
            data.setX(250 + random.nextInt(300));
            data.setFacebook(180 + random.nextInt(200));
            data.setInstagram(150 + random.nextInt(180));
            data.setYoutube(50 + random.nextInt(80));
            data.setTiktok(80 + random.nextInt(100));
            list.add(data);
        }
        return list;
    }
    
    @Override
    public SourceStatsResponse getSourceStats() {
        SourceStatsResponse stats = new SourceStatsResponse();
        stats.setPersonCount(personRepository.countActive());
        stats.setOrgCount(organizationRepository.countActive());
        stats.setWebsiteCount(websiteRepository.countActive());
        stats.setSocialCount(socialAccountRepository.countActive());
        stats.setTotalSources(stats.getPersonCount() + stats.getOrgCount() + 
                               stats.getWebsiteCount() + stats.getSocialCount());
        stats.setValidSources(stats.getTotalSources());
        return stats;
    }
}
