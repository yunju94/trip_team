package com.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CartDetailDto {
    private  Long id;//상품 아이디
    private  String itemNm;//상품명
    private  String departuredate;//여행 날짜
    private  int count;//인원
    private  int price;//금액

}
