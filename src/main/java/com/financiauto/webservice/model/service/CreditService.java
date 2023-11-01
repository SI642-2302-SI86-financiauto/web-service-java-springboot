package com.financiauto.webservice.model.service;

import com.financiauto.webservice.model.entities.Credit;

import java.util.List;

public interface CreditService {
    List<Credit> getAllCredits();
    Credit save(Credit credit);
}
