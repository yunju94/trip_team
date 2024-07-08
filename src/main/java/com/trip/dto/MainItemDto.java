package com.trip.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.trip.constant.Nature;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MainItemDto {
    private Long id;
    private String itemNm;
    private String itemDetail;
    private String imgUrl;
    private Integer price;
    private Nature nature;
    private LocalDate startDate;
    private LocalDate endDate;

    @QueryProjection // Querydsl 결과 조회 시 MainItemDto 객체로 바로 오도록 활용
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price, Nature nature,LocalDate startDate,LocalDate endDate){
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
        this.nature = nature;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
