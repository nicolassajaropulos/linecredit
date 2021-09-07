package com.tribal.linecredit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tribal.linecredit.domain.dto.ApplicationRequestDTO;
import com.tribal.linecredit.domain.dto.ApplicationResponseDTO;
import com.tribal.linecredit.services.ILineCreditService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ApplicationController.class)
public class ApplicationControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ILineCreditService iLineCreditService;

    @Test
    public void requestCreditAccepted() throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date requestDate = simpleDateFormat.parse("2021-07-19");

        ApplicationRequestDTO applicationRequestDTO = new ApplicationRequestDTO();
        applicationRequestDTO.setFoundingType("SME");
        applicationRequestDTO.setCashBalance(435.30);
        applicationRequestDTO.setMonthlyRevenue(4235.45);
        applicationRequestDTO.setRequestedCreditLine(100.00);
        applicationRequestDTO.setRequestedDate(requestDate);

        ApplicationResponseDTO applicationResponseDTO = new ApplicationResponseDTO();
        applicationResponseDTO.setAuthorized(true);
        applicationResponseDTO.setCreditAuthorized(1000D);

        given(iLineCreditService.requestLineCredit(any())).willReturn(applicationResponseDTO);

        mockMvc.perform(post("/LineCredit/application")
                .content(om.writeValueAsString(applicationRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Credit line accepted: $847.09"));
    }

}
