package com.metahospital.datacollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ServletComponentScan
public class DataCollectorApplication {
    public static void main(String[] args) {
        //just a test,it will be deleted
        //push back and delete this
        SpringApplication.run(DataCollectorApplication.class, args);
    }
}
