package com.tribal.linecredit.services;

import com.tribal.linecredit.domain.dto.ApplicationRequestDTO;
import com.tribal.linecredit.domain.dto.ApplicationResponseDTO;

public interface ILineCreditService {

    ApplicationResponseDTO requestLineCredit(ApplicationRequestDTO applicationRequestDTO) throws Exception;

}
