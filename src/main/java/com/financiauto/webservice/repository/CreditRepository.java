package com.financiauto.webservice.repository;

import com.financiauto.webservice.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<Credit, Long> {

}

