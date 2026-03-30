package com.datacollection.slave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 分调度中心启动类
 * 
 * 职责：
 * 1. 消费Kafka任务队列，选择合适节点下发采集指令
 * 2. 接收采集数据，推送到Kafka新闻数据Topic
 */
@SpringBootApplication
@EnableScheduling
public class SlaveSchedulerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SlaveSchedulerApplication.class, args);
    }
}
