package com.datacollection.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主调度中心启动类
 * 
 * 职责：
 * 1. 定时检查数据库任务表，将待执行任务推送到Kafka任务队列
 * 2. 消费Kafka新闻数据Topic，将数据写入数据库
 */
@SpringBootApplication
@EnableScheduling
public class MasterSchedulerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MasterSchedulerApplication.class, args);
    }
}
