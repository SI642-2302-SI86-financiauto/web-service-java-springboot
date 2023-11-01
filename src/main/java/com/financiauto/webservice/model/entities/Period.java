package com.financiauto.webservice.model.entities;

import com.financiauto.webservice.model.enums.GracePeriodType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Period {
    @Id
    int periodIndex; // numero de periodo
    double TEP; // Tasa Efectiva Periodica
    private GracePeriodType gracePeriodType;// tipo de Periodo de Gracia
    double openBalance; // saldo inicial
    double interest; // interes
    double quote; // cuota
    double amortization; // amortization
    double endBalance; // saldo final
}
