package com.financiauto.webservice.model.entities;

import com.financiauto.webservice.model.enums.GracePeriodType;
import com.financiauto.webservice.model.enums.PaymentFrecuency;
import com.financiauto.webservice.model.enums.RateType;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="credit")
public class Credit {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @JoinColumn(name="user_id")
    private Long userId;
    @Column(name="num_years")
    @NotNull
    private int numYears; // numero de años
    @NotNull
    @Column(name="selling_price")
    private double sellingPrice; // precio de venta
    @NotNull
    @Column(name="init_quote_percent")
    private int initQuotePercent; // porcentaje de cuota inicial
    @NotNull
    @Column(name="rate_type")
    private RateType rateType; // tipo de tasa
    @NotNull
    @Column(name="rate_value")
    private double rateValue; // valor de la tasa
    @NotNull
    @Column(name="end_quote_percent")
    private int endQuotePercent; // porcentaje de cuota final
    @NotNull
    @Column(name="payment_frecuency")
    private PaymentFrecuency paymentFrecuency; // frecuencia de pago
    @NotNull
    @Column(name="grace_period_type")
    private GracePeriodType gracePeriodType; // tipo de periodo de gracia
    @NotNull
    @Column(name="num_grace_periods")
    private int numGracePeriods; // numero de periodos de gracia
}
