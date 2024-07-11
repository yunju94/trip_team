package com.trip.controller;


import com.trip.dto.ConvertInfoDto;
import com.trip.dto.ExchangeDto;
import com.trip.service.CurrencyConverterService;
import com.trip.service.ExchangeNatureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;


@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class CurrencyConverterAPIController {
    private final CurrencyConverterService currencyConverter;

    private  final ExchangeNatureService exchangeNatureService;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.0000");

    @GetMapping(value = "/list")
    public  String excahangeList(){
        return "exchange/index";
    }


    //국가에 따른 환율을 가져오는 메소드
    @GetMapping(value = "/exchange-rates")
    public ResponseEntity getExchangeRate(@RequestParam(name = "receiveCountry") String receiveCountry){
        Double exchangeRate = currencyConverter.getCurrencyRate(receiveCountry);

        System.out.println(exchangeRate);
        return new ResponseEntity(format(exchangeRate), HttpStatus.OK);
    }

    //송금액을 가져오는 메소드
    @PostMapping(value = "/exchange-rates")
    public ResponseEntity getSendAmount(@Valid @RequestBody ConvertInfoDto convertInfo){//수취 국가 보내는 금액

        Double currency = currencyConverter.getCurrencyRate(convertInfo.getReceiveCountry());
        Double sendAmount = (currency * convertInfo.getSendAmount());
        String formatSendAmount = format(sendAmount);

        return new ResponseEntity(formatSendAmount, HttpStatus.OK);
    }

    public String format(Number number){
        return decimalFormat.format(number);
    }

    //환율을 받아서 DB로 보낼 수 없을까?
    @GetMapping(value = "/exchange")
    public ResponseEntity ExchangeR(@RequestParam(name = "PHP") String PHP,@RequestParam(name = "JPY") String JPY
                                    , @RequestParam(name = "USD") String USD,@RequestParam(name = "VND") String VND,
                                    @RequestParam(name = "MYR") String MYR){

        Double exchangeRate_PHP = currencyConverter.getCurrencyRate(PHP);

        Double exchangeRate_JPY = currencyConverter.getCurrencyRate(JPY);

        Double exchangeRate_USD = currencyConverter.getCurrencyRate(USD);

        Double exchangeRate_VND = currencyConverter.getCurrencyRate(VND);

        Double exchangeRate_MYR = currencyConverter.getCurrencyRate(MYR);


        ExchangeDto exchangeDto = new ExchangeDto( exchangeRate_JPY,exchangeRate_PHP,
                exchangeRate_USD, exchangeRate_VND,  exchangeRate_MYR);

        exchangeNatureService.savesExchange(exchangeDto);


        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }

}
