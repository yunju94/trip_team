package com.trip.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {
    private Long id; //상품코드
    private String nature; // 국내, 해외
    private String category; // 미국 일본..
    private String itemNm; // 상품명
    private String itemDetail; // 상세설명
    private String itemSellStatus; // 판매현황
    private String departuredate;//출발일
    private String arrivaldate;//도착일

    private String imgUrl;
    private Integer price;

    @QueryProjection //Querydsl 결과 조회 시 MainItemDto 객체로 바로 오도록 활용
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price,
                       String category, String nature, String departuredate, String arrivaldate) {
        this.id = id;
        this.category = category;
        this.nature = nature;
        this.itemNm=itemNm;
        this.itemDetail=itemDetail;
        this.departuredate=departuredate;
        this.arrivaldate=arrivaldate;
        this.imgUrl=imgUrl;
        this.price=price;
    }
}
