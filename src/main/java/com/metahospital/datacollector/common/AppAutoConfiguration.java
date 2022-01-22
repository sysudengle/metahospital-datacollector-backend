package com.metahospital.datacollector.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(AppContext.class)
public class AppAutoConfiguration {
}
