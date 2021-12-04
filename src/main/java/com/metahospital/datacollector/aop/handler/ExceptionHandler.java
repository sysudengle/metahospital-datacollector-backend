/*
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 */
package com.metahospital.datacollector.aop.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.metahospital.datacollector.common.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;

// 异常捕获统一处理返回码，实现HandlerExceptionResolver接口类会在主流程被调用
public class ExceptionHandler implements HandlerExceptionResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    private static final String ERROR_CODE_KEY = "errorCode";
    private static final String ERROR_MSG_KEY = "errorMessage";
    private static final String DATA_KEY = "data";


    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {

        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attributes = new HashMap<>();

        RestResponse res = RestResponse.processErrorResponse(ex);
        attributes.put(ERROR_CODE_KEY, res.getErrorCode());
        attributes.put(ERROR_MSG_KEY, res.getErrorMessage());
        attributes.put(DATA_KEY, res.getData());

        view.setAttributesMap(attributes);
        ModelAndView mv = new ModelAndView();
        mv.setView(view);

        LOGGER.info("handler accepts exception", ex);

        return mv;
    }
}
