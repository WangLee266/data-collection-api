package com.datacollection.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

/**
 * 创建任务请求
 */
@Data
public class CreateTaskRequest {
    
    @NotBlank(message = "任务名称不能为空")
    private String name;
    
    @NotBlank(message = "任务类型不能为空")
    private String type;
    
    private List<Long> sourceIds;
    private List<Long> channelIds;
    private List<Long> accountIds;
    
    private String strategy;
    private String cronExpression;
    
    private String priority = "NORMAL";
    
    private Boolean collectImages = true;
    private Boolean collectVideos = false;
    private Boolean collectComments = false;
    
    private String startTime = "immediate";
    private String scheduledTime;
    
    private String remark;
}
