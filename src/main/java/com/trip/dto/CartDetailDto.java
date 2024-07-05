package com.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class CartDetailDto {
    private  Long id;//상품 아이디
    private  String itemNm;//상품명
    private  LocalDateTime startDate;//출발 날짜
    private  int count;//인원
    private  int price;//금액

}
