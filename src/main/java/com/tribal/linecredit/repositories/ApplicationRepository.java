package com.tribal.linecredit.repositories;

import com.tribal.linecredit.domain.entities.Application;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationRepository extends CrudRepository<Application, Integer> {

    Application findApplicationByFoundingType(String foundingType);

}
