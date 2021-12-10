/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.aop.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.metahospital.datacollector.common.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;

// 异常捕获统一处理返回码，实现HandlerExceptionResolver接口类会在主流程被调用
@Component
public class ExceptionHandler implements HandlerExceptionResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    private static final String RET_CODE_KEY = "retcode";
    private static final String MSG_KEY = "message";
    private static final String DATA_KEY = "data";


    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {

        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attributes = new HashMap<>();

        RestResponse res = RestResponse.processErrorResponse(ex);
        attributes.put(RET_CODE_KEY, res.getRetcode());
        attributes.put(MSG_KEY, res.getMessage());
        attributes.put(DATA_KEY, res.getData());

        view.setAttributesMap(attributes);
        ModelAndView mv = new ModelAndView();
        mv.setView(view);

        LOGGER.info("handler accepts exception", ex);

        return mv;
    }
}
