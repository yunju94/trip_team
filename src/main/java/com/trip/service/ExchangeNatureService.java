package com.trip.service;


import com.trip.dto.ExchangeDto;
import com.trip.entity.Exchange;
import com.trip.repository.ExchangeNatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.trip.entity.Exchange.createExchange;

@Service
@RequiredArgsConstructor
@Transactional
public class ExchangeNatureService {
    private final ExchangeNatureRepository exchangeNatureRepository;

    public void  savesExchange(ExchangeDto exchangeDto){
        Exchange exchange = createExchange(exchangeDto.getJPY(), exchangeDto.getPHP(),
                exchangeDto.getUSD(), exchangeDto.getVND(),exchangeDto.getMYR());
        exchangeNatureRepository.save(exchange);
    }




}
