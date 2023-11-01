package com.financiauto.webservice.service;

import com.financiauto.webservice.model.entities.Credit;
import com.financiauto.webservice.model.entities.Period;
import com.financiauto.webservice.model.enums.GracePeriodType;
import com.financiauto.webservice.model.enums.RateType;
import com.financiauto.webservice.model.repository.CreditRepository;
import com.financiauto.webservice.model.service.CreditService;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CreditServiceImpl implements CreditService {
    private final CreditRepository creditRepository;

    public CreditServiceImpl(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    @Override
    public List<Credit> getAllCredits() {
        return creditRepository.findAll();
    }

    @Override
    public Credit save(Credit credit) {
        return creditRepository.save(credit);
    }

    public List<Period> getPaymentScheduleById(Long id) {
        Credit credit = creditRepository.findById(id).orElse(null);
        if (credit == null) {
            return null;
        }
        return calculatePaymentSchedule(credit);
    }

    private List<Period> calculatePaymentSchedule(Credit credit) {
        int numPeriod = 0;
        double rateEffectiveValue = 0.0; // TEP

        double percentCI = (double) credit.getInitQuotePercent() / 100;
        double rateValue = credit.getRateValue() / 100.0;
        double percentCF = (double) credit.getEndQuotePercent() / 100;
        /*
         * 1. Calculate the number of periods
         * @parameter numPeriod
         */
        switch (credit.getPaymentFrecuency()) {
            case FORTNIGHTLY:
                numPeriod = credit.getNumYears() * 24;
                break;
            case MONTHLY:
                numPeriod = credit.getNumYears() * 12;
                break;
            case QUARTERLY:
                numPeriod = credit.getNumYears() * 4;
                break;
            case SEMIANNUALLY:
                numPeriod = credit.getNumYears() * 2;
                break;
            case ANNUALLY:
                numPeriod = credit.getNumYears();
                break;
        }
        // calcular cuota inicial y final
        double initialQuoteValue = credit.getSellingPrice() * percentCI;
        double finalQuoteValue = credit.getSellingPrice() * percentCF;
        /*
         * 2. Calculate the effective rate
         */
        DecimalFormat df = new DecimalFormat("#.#######");
        if (credit.getRateType() == RateType.NOMINAL) {
            // NOMINAL
            switch (credit.getPaymentFrecuency()) {
                case FORTNIGHTLY:
                    double resultF = (Math.pow(1 + (rateValue / 24), 24) - 1);
                    rateEffectiveValue = Double.parseDouble(df.format(resultF));
                    break;
                case MONTHLY:
                    double resultM = (Math.pow(1 + (rateValue / 12), 12) - 1);
                    rateEffectiveValue = Double.parseDouble(df.format(resultM));
                    break;
                case QUARTERLY:
                    double resultQ = (Math.pow(1 + (rateValue / 4), 4) - 1);
                    rateEffectiveValue = Double.parseDouble(df.format(resultQ));
                    break;
                case SEMIANNUALLY:
                    double resultS = (Math.pow(1 + (rateValue / 2), 2) - 1);
                    rateEffectiveValue = Double.parseDouble(df.format(resultS));
                    break;
                case ANNUALLY:
                    rateEffectiveValue = credit.getRateValue();
                    break;
            }
        } else {
            // EFFECTIVE
            switch (credit.getPaymentFrecuency()) {
                case FORTNIGHTLY:
                    double resultF = ((Math.pow(1 + rateValue, (double) 15 / 360)) - 1);
                    rateEffectiveValue = Double.parseDouble(df.format(resultF));
                    break;
                case MONTHLY:
                    double resultM = ((Math.pow(1 + rateValue, (double) 30 / 360)) - 1);
                    rateEffectiveValue = Double.parseDouble(df.format(resultM));
                    break;
                case QUARTERLY:
                    double resultQ = ((Math.pow(1 + rateValue, (double) 90 / 360)) - 1);
                    rateEffectiveValue = Double.parseDouble(df.format(resultQ));
                    break;
                case SEMIANNUALLY:
                    double resultS = ((Math.pow(1 + rateValue, (double) 180 / 360)) - 1);
                    rateEffectiveValue = Double.parseDouble(df.format(resultS));
                    break;
                case ANNUALLY:
                    double resultA = ((Math.pow(1 + rateValue, (double) 360 / 360)) - 1);
                    rateEffectiveValue = Double.parseDouble(df.format(resultA));
                    break;
            }
        }
        /*
         * 3. Calculate the const rate
         */
        double varf1 = f1(rateEffectiveValue, numPeriod, credit.getNumGracePeriods());
        double varf2 = f2(finalQuoteValue, rateEffectiveValue, numPeriod, credit.getNumGracePeriods());
        double staticFee = (credit.getSellingPrice() - initialQuoteValue - varf2) / varf1;

        List<Period> paymentSchedule = new ArrayList<Period>();
        double memoryCacheValue = 0;

        int numGracePeriods = credit.getNumGracePeriods();

        for (var i = 1; i <= numPeriod; i++) {
            Period period = new Period();
            if (numGracePeriods > 0) {
                if (numGracePeriods == 1) {
                    switch (credit.getGracePeriodType()) {
                        case TOTALY -> {
                            period.setPeriodIndex(i);
                            period.setTEP(rateEffectiveValue);
                            period.setGracePeriodType(GracePeriodType.TOTALY);
                            period.setOpenBalance(credit.getSellingPrice() - initialQuoteValue);
                            period.setInterest(period.getOpenBalance() * period.getTEP());
                            period.setQuote(0);
                            period.setAmortization(0);
                            period.setEndBalance(period.getOpenBalance() + period.getInterest());
                            paymentSchedule.add(period);
                            memoryCacheValue = period.getEndBalance();
                        }
                        case PARTIALLY -> {
                            period.setPeriodIndex(i);
                            period.setTEP(rateEffectiveValue);
                            period.setGracePeriodType(GracePeriodType.PARTIALLY);
                            period.setOpenBalance(credit.getSellingPrice() - initialQuoteValue);
                            period.setInterest(period.getOpenBalance() * period.getTEP());
                            period.setQuote(period.getInterest());
                            period.setAmortization(0);
                            period.setEndBalance(period.getOpenBalance());
                            paymentSchedule.add(period);
                            memoryCacheValue = period.getEndBalance();
                        }
                        case NONE -> {
                            period.setPeriodIndex(i);
                            period.setTEP(rateEffectiveValue);
                            period.setGracePeriodType(GracePeriodType.NONE);
                            period.setOpenBalance(credit.getSellingPrice() - initialQuoteValue);
                            period.setInterest(period.getOpenBalance() * period.getTEP());
                            period.setQuote(staticFee);
                            period.setAmortization(period.getQuote() - period.getInterest());
                            period.setEndBalance(period.getOpenBalance() - period.getAmortization());
                            paymentSchedule.add(period);
                            memoryCacheValue = period.getEndBalance();
                        }
                    }

                } else {
                    switch (credit.getGracePeriodType()) {
                        case TOTALY -> {
                            period.setPeriodIndex(i);
                            period.setTEP(rateEffectiveValue);
                            period.setGracePeriodType(GracePeriodType.TOTALY);
                            period.setOpenBalance(memoryCacheValue);
                            period.setInterest(period.getOpenBalance() * period.getTEP());
                            period.setQuote(0);
                            period.setAmortization(0);
                            period.setEndBalance(period.getOpenBalance() + period.getInterest());
                            paymentSchedule.add(period);
                            memoryCacheValue = period.getEndBalance();
                        }
                        case PARTIALLY -> {
                            period.setPeriodIndex(i);
                            period.setTEP(rateEffectiveValue);
                            period.setGracePeriodType(GracePeriodType.PARTIALLY);
                            period.setOpenBalance(memoryCacheValue);
                            period.setInterest(period.getOpenBalance() * period.getTEP());
                            period.setQuote(period.getInterest());
                            period.setAmortization(0);
                            period.setEndBalance(period.getOpenBalance());
                            paymentSchedule.add(period);
                            memoryCacheValue = period.getEndBalance();
                        }
                        case NONE -> {
                            period.setPeriodIndex(i);
                            period.setTEP(rateEffectiveValue);
                            period.setGracePeriodType(GracePeriodType.NONE);
                            period.setOpenBalance(memoryCacheValue);
                            period.setInterest(period.getOpenBalance() * period.getTEP());
                            period.setQuote(staticFee);
                            period.setAmortization(period.getQuote() - period.getInterest());
                            period.setEndBalance(period.getOpenBalance() - period.getAmortization());
                            paymentSchedule.add(period);
                            memoryCacheValue = period.getEndBalance();
                        }
                    }
                }
                numGracePeriods--;
            } else {
                if (i == 1) {
                    period.setPeriodIndex(i);
                    period.setTEP(rateEffectiveValue);
                    period.setGracePeriodType(GracePeriodType.NONE);
                    period.setOpenBalance(credit.getSellingPrice() - initialQuoteValue);
                    period.setInterest(period.getOpenBalance() * period.getTEP());
                    period.setQuote(staticFee);
                    period.setAmortization(period.getQuote() - period.getInterest());
                    period.setEndBalance(period.getOpenBalance() - period.getAmortization());
                    paymentSchedule.add(period);
                    memoryCacheValue = period.getEndBalance();
                } else {
                    period.setPeriodIndex(i);
                    period.setTEP(rateEffectiveValue);
                    period.setGracePeriodType(GracePeriodType.NONE);
                    period.setOpenBalance(memoryCacheValue);
                    period.setInterest(period.getOpenBalance() * period.getTEP());
                    period.setQuote(staticFee);
                    period.setAmortization(period.getQuote() - period.getInterest());
                    period.setEndBalance(period.getOpenBalance() - period.getAmortization());
                    paymentSchedule.add(period);
                    memoryCacheValue = period.getEndBalance();
                }
            }
        }

        Period finalPeriod = new Period();
        finalPeriod.setPeriodIndex(numPeriod + 1);
        finalPeriod.setTEP(0);
        finalPeriod.setGracePeriodType(GracePeriodType.NONE);
        finalPeriod.setOpenBalance(memoryCacheValue);
        finalPeriod.setInterest(0);
        finalPeriod.setQuote(memoryCacheValue);
        finalPeriod.setAmortization(memoryCacheValue);
        finalPeriod.setEndBalance(finalPeriod.getQuote() - finalPeriod.getAmortization());
        paymentSchedule.add(finalPeriod);

        return paymentSchedule;
    }

    private static double f1(double rateEffectivePeriod, int numPeriods, int numGracePeriods) {
        double pow;
        if (numGracePeriods > 0) {
            pow = Math.pow(1 + rateEffectivePeriod, numPeriods - numGracePeriods);
        } else {
            pow = Math.pow(1 + rateEffectivePeriod, numPeriods);
        }
        System.out.printf("f1: %f\n", (pow - 1) / (rateEffectivePeriod * pow));
        return ((pow - 1) / (rateEffectivePeriod * pow));
    }

    private static double f2(double endQuoteValue, double rateEffectivePeriod, int numPeriods, int numGracePeriods) {
        double pow;
        if (numGracePeriods > 0) {
            pow = Math.pow(1 + rateEffectivePeriod, numPeriods - numGracePeriods);
        } else {
            pow = Math.pow(1 + rateEffectivePeriod, numPeriods);
        }
        System.out.printf("f2: %f\n", (endQuoteValue / pow));
        return (endQuoteValue / pow);
    }
}
