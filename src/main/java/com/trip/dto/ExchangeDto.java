package com.trip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeDto {
    private Long id;
    private Double JPY;//일본
    private Double PHP;//필리핀
    private Double USD;//미국
    private Double VND;//베트남
    private Double MYR;//말레이시아

    public ExchangeDto(Double JPY, Double PHP, Double USD, Double VND, Double MYR){
        this.MYR= MYR;
        this.PHP = PHP;
        this.USD = USD;
        this.VND = VND;
        this.JPY = JPY;
    }


}
