package com.financiauto.webservice.model.repository;

import com.financiauto.webservice.model.entities.Credit;
import com.financiauto.webservice.model.entities.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
    List<Period> getPaymentScheduleById(Long id);
}

