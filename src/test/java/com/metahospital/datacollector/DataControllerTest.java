/*
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 */
package com.metahospital.datacollector;

import com.metahospital.datacollector.controller.DataController;
import com.metahospital.datacollector.service.impl.DataServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class DataControllerTest {
    MockMvc mockMvc;
    DataController dataController;
    DataServiceImpl dataService;

    @Before
    public void setupMock() {
        dataController = new DataController();
        dataService = Mockito.spy(new DataServiceImpl());
        dataController.setDataService(dataService);

        mockMvc = MockMvcBuilders.standaloneSetup(dataController).build();
    }

    @Test
    public void testDemo() throws Exception {
//        when(dataService.IsDataValid("naotaitao")).thenReturn(true);
//
//        mockMvc.perform(get("/api/data/test?")
//                .header("Dip-Authentication", "naotaitao"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk());
    }
}
