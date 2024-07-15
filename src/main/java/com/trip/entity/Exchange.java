package com.trip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "exchange")
@Getter
@Setter
@ToString
public class Exchange {
    @Id
    @GeneratedValue
    @Column(name = "Exchange_id")
    private Long id;
    private Double JPY;//일본
    private Double PHP;//필리핀
    private Double USD;//미국
    private Double VND;//베트남
    private Double MYR;//말레이시아
    private LocalDate date;//날짜


    public static Exchange  createExchange( Double JPY, Double PHP, Double USD, Double VND, Double MYR){
        Exchange exchange = new Exchange();
        exchange.JPY = JPY;
        exchange.MYR = MYR;
        exchange.PHP = PHP;
        exchange.USD = USD;
        exchange.VND = VND;
        exchange.date = LocalDate.now();

        return exchange;

    }

}
