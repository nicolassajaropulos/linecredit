package com.tribal.linecredit.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "applications", schema = "linecredit")
public class Application {

    public static final String FOUNDING_TYPE_SME = "SME";
    public static final String FOUNDING_TYPE_STARTUP = "Startup";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String foundingType;

    private Double recommendedCredit;

    private Date requestedDate;

}