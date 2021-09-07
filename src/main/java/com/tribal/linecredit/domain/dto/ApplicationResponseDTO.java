package com.tribal.linecredit.domain.dto;

import lombok.Data;

@Data
public class ApplicationResponseDTO {

    private boolean authorized;
    private Double creditAuthorized;
}
