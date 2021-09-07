package com.tribal.linecredit.services.impl;

import com.tribal.linecredit.domain.dto.ApplicationRequestDTO;
import com.tribal.linecredit.domain.dto.ApplicationResponseDTO;
import com.tribal.linecredit.domain.entities.Application;
import com.tribal.linecredit.repositories.ApplicationRepository;
import com.tribal.linecredit.services.ILineCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineCreditServiceImpl implements ILineCreditService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public ApplicationResponseDTO requestLineCredit(ApplicationRequestDTO applicationRequestDTO) throws Exception {

        double recommendedCredit;

        switch (applicationRequestDTO.getFoundingType()) {
            case Application.FOUNDING_TYPE_SME:

                recommendedCredit = applicationRequestDTO.getMonthlyRevenue() / 5;

                break;
            case Application.FOUNDING_TYPE_STARTUP:

                double rCashBalance = applicationRequestDTO.getCashBalance() / 3;
                double rMonthlyRevenue = applicationRequestDTO.getMonthlyRevenue() / 5;

                recommendedCredit = Math.max(rCashBalance, rMonthlyRevenue);

                break;
            default:
                throw new Exception("Founding type not valid");
        }

        Application application = applicationRepository.findApplicationByFoundingType(applicationRequestDTO.getFoundingType());

        ApplicationResponseDTO applicationResponseDTO = new ApplicationResponseDTO();
        applicationResponseDTO.setAuthorized(false);

        if (recommendedCredit > applicationRequestDTO.getRequestedCreditLine()) {
            applicationResponseDTO.setAuthorized(true);

            if (application != null) {
                applicationResponseDTO.setCreditAuthorized(application.getRecommendedCredit());
            } else {

                application = Application
                        .builder()
                        .foundingType(applicationRequestDTO.getFoundingType())
                        .recommendedCredit(recommendedCredit)
                        .requestedDate(applicationRequestDTO.getRequestedDate())
                        .build();

                applicationRepository.save(application);
                applicationResponseDTO.setCreditAuthorized(recommendedCredit);
            }
        }

        return applicationResponseDTO;
    }

}
