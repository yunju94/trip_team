package com.trip.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.trip.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartViewDto {

    private String itemNm;
    private String imgUrl;


    @QueryProjection
    public  CartViewDto(String itemNm, String imgUrl){

        this.itemNm = itemNm;
        this.imgUrl = imgUrl;


    }



}
