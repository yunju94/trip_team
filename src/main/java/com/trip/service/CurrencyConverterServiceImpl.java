package com.trip.service;


import com.trip.dto.CurrencyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CurrencyConverterServiceImpl implements CurrencyConverterService {
    @Value("${currencyLayer.source}")
    private  String sendCountry;
    private  final CurrencyAPIService currencyAPIService;



    @Override
    public Double getCurrencyRate(String receiveCountry) {
        CurrencyDto currency = currencyAPIService.getCurrency();
        String sendReceiveCountry = sendCountry + receiveCountry;

        Double convertedCurrency = currency.getQuotes().get(sendReceiveCountry);


        return convertedCurrency;
    }


}
