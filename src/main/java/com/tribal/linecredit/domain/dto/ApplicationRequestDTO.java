package com.tribal.linecredit.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ApplicationRequestDTO {

    private String foundingType;

    private Double cashBalance;

    private Double monthlyRevenue;

    private Double requestedCreditLine;

    private Date requestedDate;

}
