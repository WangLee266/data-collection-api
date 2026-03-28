package com.datacollection.dto;

import lombok.Data;

/**
 * 分页请求参数
 */
@Data
public class PageRequest {
    
    private Integer page = 1;
    private Integer size = 10;
    private String keyword;
    private String sortBy = "createTime";
    private String sortOrder = "desc";
    
    public Integer getOffset() {
        return (page - 1) * size;
    }
}
