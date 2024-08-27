package com.trip.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventDto {

    private  Long id;//아이템 아이디

    private  String itemNm;//제목

    private String imgUrl;//이미지

    private  String itemDetail;//상세 설명

    private  String search;//검색 단어
    private  int price;

    @QueryProjection
    public EventDto(Long id, String itemNm,String imgUrl,  String itemDetail, int price){
        this.id = id;
        this.itemNm = itemNm;
        this.imgUrl = imgUrl;
        this.itemDetail = itemDetail;
        this.price = price;


    }

}
