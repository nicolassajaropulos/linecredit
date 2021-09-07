package com.tribal.linecredit.services;

import com.tribal.linecredit.domain.dto.ApplicationRequestDTO;
import com.tribal.linecredit.domain.dto.ApplicationResponseDTO;
import com.tribal.linecredit.domain.entities.Application;
import com.tribal.linecredit.repositories.ApplicationRepository;
import com.tribal.linecredit.services.impl.LineCreditServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class LineCreditServiceImplTest {

    @TestConfiguration
    static class LineCreditServiceImplTestContextConfiguration {

        @Bean
        public ILineCreditService iLineCreditService() {
            return new LineCreditServiceImpl();
        }
    }

    @Autowired
    private ILineCreditService iLineCreditService;

    @MockBean
    private ApplicationRepository applicationRepository;

    @Test(expected = Exception.class)
    public void creditLineRequestWithNotValidFoundingType() throws Exception {
        ApplicationRequestDTO applicationRequestDTO = new ApplicationRequestDTO();
        applicationRequestDTO.setFoundingType("NVFT");
        applicationRequestDTO.setCashBalance(435.30);
        applicationRequestDTO.setMonthlyRevenue(4235.45);
        applicationRequestDTO.setRequestedCreditLine(100.00);
        applicationRequestDTO.setRequestedDate(new Date());

        iLineCreditService.requestLineCredit(applicationRequestDTO);
    }

    @Test
    public void creditLineSMEAcceptedFirstTime() throws Exception {
        ApplicationRequestDTO applicationRequestDTO = new ApplicationRequestDTO();
        applicationRequestDTO.setFoundingType("SME");
        applicationRequestDTO.setCashBalance(435.30);
        applicationRequestDTO.setMonthlyRevenue(4235.45);
        applicationRequestDTO.setRequestedCreditLine(100.00);
        applicationRequestDTO.setRequestedDate(new Date());


        when(applicationRepository.findApplicationByFoundingType(anyString())).thenReturn(null);

        ApplicationResponseDTO applicationResponseDTO = iLineCreditService.requestLineCredit(applicationRequestDTO);

        Assert.assertTrue(applicationResponseDTO.isAuthorized());
    }

    @Test
    public void creditLineStartupAcceptedFirstTime() throws Exception {
        ApplicationRequestDTO applicationRequestDTO = new ApplicationRequestDTO();
        applicationRequestDTO.setFoundingType("Startup");
        applicationRequestDTO.setCashBalance(435.30);
        applicationRequestDTO.setMonthlyRevenue(4235.45);
        applicationRequestDTO.setRequestedCreditLine(100.00);
        applicationRequestDTO.setRequestedDate(new Date());


        when(applicationRepository.findApplicationByFoundingType(anyString())).thenReturn(null);

        ApplicationResponseDTO applicationResponseDTO = iLineCreditService.requestLineCredit(applicationRequestDTO);

        Assert.assertTrue(applicationResponseDTO.isAuthorized());
    }

    @Test
    public void creditLinePrevAcceptedWithSameRecommendedCredit() throws Exception {
        ApplicationRequestDTO applicationRequestDTO = new ApplicationRequestDTO();
        applicationRequestDTO.setFoundingType("SME");
        applicationRequestDTO.setCashBalance(435.30);
        applicationRequestDTO.setMonthlyRevenue(4235.45);
        applicationRequestDTO.setRequestedCreditLine(100.00);
        applicationRequestDTO.setRequestedDate(new Date());

        Application application = new Application();
        application.setId(1);
        application.setFoundingType("SME");
        application.setRequestedDate(new Date());
        application.setRecommendedCredit(1000D);

        when(applicationRepository.findApplicationByFoundingType(anyString())).thenReturn(application);

        ApplicationResponseDTO applicationResponseDTO = iLineCreditService.requestLineCredit(applicationRequestDTO);

        Assert.assertEquals(application.getRecommendedCredit(), applicationResponseDTO.getCreditAuthorized());
    }

}
