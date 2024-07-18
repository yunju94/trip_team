package com.trip.service;


import com.trip.dto.CurrencyDto;
import com.trip.repository.ExchangeNatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CurrencyConverterServiceImpl implements CurrencyConverterService {
    @Value("${currencyLayer.source}")
    private  String sendCountry;
    private  final CurrencyAPIService currencyAPIService;

    private  final ExchangeNatureRepository exchangeNatureRepository;

    @Override
    public Double getCurrencyRate(String receiveCountry) {
        CurrencyDto currency = currencyAPIService.getCurrency();
        String sendReceiveCountry = sendCountry + receiveCountry;//"KRW" + "JPY"
        Double convertedCurrency = currency.getQuotes().get(sendReceiveCountry);
        return convertedCurrency;
    }

    @Override
    public List<Double> getPHPDB() {
        List<Double> PHP = new ArrayList<>();
        LocalDate date = LocalDate.now();
        List<String> str = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = date.minusDays(i);
            Double value = 0.0;
            str= exchangeNatureRepository.findByPHP(currentDate);
            if (!str.isEmpty()) {
                value = Double.valueOf(str.getLast());
            }
            System.out.println(value);
            PHP.add(value);
        }
        return PHP;
    }

    @Override
    public List<Double> getJPYDB() {
        List<Double> JPY = new ArrayList<>();
        LocalDate date = LocalDate.now();
        List<String> str = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = date.minusDays(i);
            Double value = 0.0;
            str=exchangeNatureRepository.findByJPY(currentDate);
            if (!str.isEmpty()) {
                value = Double.valueOf(str.getLast());
            }
            JPY.add(value);
        }
        return JPY;
    }

    @Override
    public List<Double> getUSDDB() {
        List<Double> USD = new ArrayList<>();
        LocalDate date = LocalDate.now();
        List<String> str = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = date.minusDays(i);
            Double value = 0.0;
            str= exchangeNatureRepository.findByUSD(currentDate);
            if (!str.isEmpty()) {
                value = Double.valueOf(str.getLast());
            }
            USD.add(value);
        }
        return USD;
    }

    @Override
    public List<Double> getVNDDB() {
        List<Double> VND = new ArrayList<>();
        LocalDate date = LocalDate.now();
        List<String> str = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = date.minusDays(i);
            Double value = 0.0;
            str=exchangeNatureRepository.findByVND(currentDate);
            if (!str.isEmpty()) {
                value = Double.valueOf(str.getLast());
            }
            VND.add(value);
        }
        return VND;
    }

    @Override
    public List<Double> getMYRDB() {
        List<Double> MYR = new ArrayList<>();
        LocalDate date = LocalDate.now();
        List<String> str = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = date.minusDays(i);
            System.out.println(currentDate);
            Double value = 0.0;
            str=exchangeNatureRepository.findByMYR(currentDate);
            System.out.println(str);
            if (!str.isEmpty()) {
                value = Double.valueOf(str.getLast());
            }
            MYR.add(value);
        }
        System.out.println(MYR);
        return MYR;
    }

}
