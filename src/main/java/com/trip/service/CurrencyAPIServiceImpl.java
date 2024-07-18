package com.trip.service;

import com.trip.dto.CurrencyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class CurrencyAPIServiceImpl implements CurrencyAPIService {
    @Value("${currencyLayer.accessKey}")
    private String accessKey;//api 접근 키
    @Value("${currencyLayer.url}")
    private String url;//api엔드 포인트 주소
    @Value("${currencyLayer.source}")
    private String source;//기준 통화
    @Value("${currencyLayer.currencies}")
    private String currencies;//변화할 통화
    @Value("${currencyLayer.cycleTime}")
    private int cycleTime;//api호출 주기



    @Autowired
    RestTemplate restTemplate;
    private CurrencyDto currency;



    @Override
    public CurrencyDto getCurrency() {
        if(ischeck()){//api 호출
            currency = restTemplate.getForObject(
                    url + "?access_key=" + accessKey
                    + "&source=" + source
                    + "&currencies=" + currencies,
                    CurrencyDto.class);
        }
        return currency;
    }
    //현재 시간과 API를 통해 호출한 timestamp의 차이가
    //application.properties에 저장해 놓은 주기 시간보다 크면 API를 호출하도록 하는 메소드
    private boolean ischeck(){
        if(currency == null){
            return true;
        }
        long currentTime = TimeUnit.MICROSECONDS.toMinutes(System.currentTimeMillis());
        return currentTime - currency.getTimestamp() > cycleTime;
    }
}
