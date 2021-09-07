package com.tribal.linecredit.controllers;

import com.tribal.linecredit.domain.dto.ApplicationRequestDTO;
import com.tribal.linecredit.domain.dto.ApplicationResponseDTO;
import com.tribal.linecredit.domain.entities.Application;
import com.tribal.linecredit.services.ILineCreditService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("application")
public class ApplicationController {

    private int REJECTED_TIMES = 0;

    private final Bucket bucketAccepted;
    private final Bucket bucketRejected;

    private final NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

    @Autowired
    private ILineCreditService iLineCreditService;

    public ApplicationController() {
        Bandwidth limit = Bandwidth.classic(3, Refill.intervally(3, Duration.ofMinutes(2)));
        this.bucketAccepted = Bucket4j.builder()
                .addLimit(limit)
                .build();

        limit = Bandwidth.classic(1, Refill.intervally(1, Duration.ofSeconds(30)));
        this.bucketRejected = Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping()
    public ResponseEntity<String> requestCredit(@RequestBody ApplicationRequestDTO applicationRequestDTO) throws Exception {

        ApplicationResponseDTO applicationResponseDTO = iLineCreditService.requestLineCredit(applicationRequestDTO);
        boolean LINECREDIT_ACCEPTED = applicationResponseDTO.isAuthorized();

        if (LINECREDIT_ACCEPTED && !bucketAccepted.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

        if (!LINECREDIT_ACCEPTED) {

            REJECTED_TIMES++;

            if(!bucketRejected.tryConsume(1)){
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
            }

            if (REJECTED_TIMES == 3) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("A sales agent will contact you");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Credit line rejected");
            }
        }



        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Credit line accepted: " + numberFormat.format(applicationResponseDTO.getCreditAuthorized()));
    }

}
