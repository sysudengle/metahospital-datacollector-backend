/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.metahospital.datacollector.aop.logger.RequestLogger;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = { "com.metahospital.datacollector.controller" })
@Import(value = {RequestLogger.class})
public class WebConfig extends WebMvcConfigurerAdapter {
    // 跨域处理
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // allow multiple host access from browsers
        registry.addMapping("/**").allowCredentials(true);
    }
}
