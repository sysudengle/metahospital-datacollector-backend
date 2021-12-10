/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.aop.logger;

import java.util.ArrayList;
import java.util.List;

import com.metahospital.datacollector.common.RestResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component("requestLogger")
public class RequestLogger {
    public static final Logger LOGGER = LoggerFactory.getLogger(RequestLogger.class);

    @Value("${profiles.active}")
    private String mode;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void mappingAnnotation() {
    }

    @Pointcut("within(com.metahospital.datacollector.controller.DataController)")
    public void controllerScope() {
    }

    @AfterReturning(pointcut = "controllerScope() && mappingAnnotation()", returning = "response")
    public void actionAfterRequest(JoinPoint joinPoint, RestResponse response) {
        if ("dev".equals(mode)) {
            String logInfo = String.format("Method: %s",
                    joinPoint.getSignature().getName());
            LOGGER.info(logInfo);

            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                if (arg == null) {
                    continue;
                }
                LOGGER.info("arg: " + convertArgsToStr(arg));
            }
        }

        LOGGER.info(String.format("method: %s, return status: %d",
                joinPoint.getSignature().getName(),
                response.getRetcode()));
    }

    private String convertArgsToStr(Object arg) {
        if (arg instanceof List) {
            List items = (List) arg;
            List<String> argStrs = new ArrayList<>();
            for (Object item : items) {
                if (item instanceof String) {
                    argStrs.add((String) item);
                } else {
                    argStrs.add(item.toString());
                }
            }

            return argStrs.toString();
        }

        return arg.toString();
    }
}
