package com.financiauto.webservice.model;

import jakarta.persistence.*;

@Entity
@Table(name="credit")
public class Credit {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long vehicle_value;
    private Long init_value;
    private Long rate_value;
    private Long paytime;
    private String grace_period;
    private Long grace_time;
    private Long COK;
    private Long last_value;
    private String currency;
    private String pay_frequency;
    private String rate_type;

    public Credit(){

    }

    public Credit(Long id, Long vehicle_value, Long init_value, Long rate_value, Long paytime, String grace_period, Long grace_time, Long COK, Long last_value, String currency, String pay_frequency, String rate_type) {
        this.id = id;
        this.vehicle_value = vehicle_value;
        this.init_value = init_value;
        this.rate_value = rate_value;
        this.paytime = paytime;
        this.grace_period = grace_period;
        this.grace_time = grace_time;
        this.COK = COK;
        this.last_value = last_value;
        this.currency = currency;
        this.pay_frequency = pay_frequency;
        this.rate_type = rate_type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicle_value() {
        return vehicle_value;
    }

    public void setVehicle_value(Long vehicle_value) {
        this.vehicle_value = vehicle_value;
    }

    public Long getInit_value() {
        return init_value;
    }

    public void setInit_value(Long init_value) {
        this.init_value = init_value;
    }

    public Long getRate_value() {
        return rate_value;
    }

    public void setRate_value(Long rate_value) {
        this.rate_value = rate_value;
    }

    public Long getPaytime() {
        return paytime;
    }

    public void setPaytime(Long paytime) {
        this.paytime = paytime;
    }

    public String getGrace_period() {
        return grace_period;
    }

    public void setGrace_period(String grace_period) {
        this.grace_period = grace_period;
    }

    public Long getGrace_time() {
        return grace_time;
    }

    public void setGrace_time(Long grace_time) {
        this.grace_time = grace_time;
    }

    public Long getCOK() {
        return COK;
    }

    public void setCOK(Long COK) {
        this.COK = COK;
    }

    public Long getLast_value() {
        return last_value;
    }

    public void setLast_value(Long last_value) {
        this.last_value = last_value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPay_frequency() {
        return pay_frequency;
    }

    public void setPay_frequency(String pay_frequency) {
        this.pay_frequency = pay_frequency;
    }

    public String getRate_type() {
        return rate_type;
    }

    public void setRate_type(String rate_type) {
        this.rate_type = rate_type;
    }
}
