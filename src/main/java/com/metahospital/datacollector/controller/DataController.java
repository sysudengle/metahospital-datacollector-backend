/*
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 */
package com.metahospital.datacollector.controller;

import com.metahospital.datacollector.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.metahospital.datacollector.service.DataService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RestController
@PropertySource("classpath:application.properties")
public class DataController {
    @Autowired
    private DataService dataService;

    @GetMapping(value = "/holly/test", produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<String> hollytest(@RequestParam(value = "id", required = false) String id, @RequestParam("name") String name) {
        return new RestResponse<>(dataService.TestMergeData(id, name));
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

}
