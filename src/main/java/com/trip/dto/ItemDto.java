package com.trip.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemDto {
    private Long id; //상품코드
    private String Nature; // 국내, 해외
    private String Category; // 미국 일본..
    private String itemNm; // 상품명
    private int price; // 가격
    private int stockNumber; // 인원 수
    private String itemDetail; // 상세설명
    private String itemSellStatus; // 판매현황

}
