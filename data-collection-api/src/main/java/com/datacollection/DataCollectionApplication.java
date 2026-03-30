package com.datacollection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 开源数据采集系统主启动类
 * 
 * @author DataCollection Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableScheduling
public class DataCollectionApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(DataCollectionApplication.class, args);
    }
}
