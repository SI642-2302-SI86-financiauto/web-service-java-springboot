package com.financiauto.webservice.controller;

import com.financiauto.webservice.model.entities.Credit;
import com.financiauto.webservice.model.entities.Period;
import com.financiauto.webservice.service.CreditServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/credit")
public class CreditController {
    private final CreditServiceImpl creditService;

    public CreditController(CreditServiceImpl creditService) {
        this.creditService = creditService;
    }

    @GetMapping
    public List<Credit> getAllCredits() {
        return creditService.getAllCredits();
    }

    @GetMapping("/{id}/schedule")
    public ResponseEntity<List<Period>> getPaymentScheduleById(@PathVariable Long id) {
        List<Period> schedule =  creditService.getPaymentScheduleById(id);
        return ResponseEntity.ok(schedule);
    }

    @PostMapping("/create")
    public Credit save(@RequestBody Credit credit) {
        return creditService.save(credit);
    }
}
