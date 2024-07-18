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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.List;


@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class CurrencyConverterAPIController {
    private final CurrencyConverterService currencyConverter;

    private  final ExchangeNatureService exchangeNatureService;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.0000");

    private  final  CurrencyConverterService currencyConverterService;

    @GetMapping(value = "/list")
    public  String excahangeList(Model model){
        List<Double> yValues1 =  currencyConverterService.getPHPDB();
        List<Double> yValues2 =  currencyConverterService.getJPYDB();
        List<Double> yValues3 =  currencyConverterService.getUSDDB();
        List<Double> yValues4 =  currencyConverterService.getVNDDB();
        List<Double> yValues5 =  currencyConverterService.getMYRDB();
        model.addAttribute("yValues1", yValues1);
        model.addAttribute("yValues2", yValues2);
        model.addAttribute("yValues3", yValues3);
        model.addAttribute("yValues4", yValues4);
        model.addAttribute("yValues5", yValues5);

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
    public ResponseEntity ExchangeR(@RequestParam(name = "PHP") String PHP,
                                    @RequestParam(name = "JPY") String JPY,
                                    @RequestParam(name = "USD") String USD,
                                    @RequestParam(name = "VND") String VND,
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
