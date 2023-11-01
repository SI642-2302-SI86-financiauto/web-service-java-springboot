package com.financiauto.webservice.model.repository;

import com.financiauto.webservice.model.entities.Period;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodRepository extends JpaRepository<Period, Long> {
}
