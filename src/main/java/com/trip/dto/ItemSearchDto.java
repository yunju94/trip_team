package com.trip.dto;


import com.trip.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {
    private String placeSearch;// 여행지
    private String startPlace; // 출발지
    private String datefilter; //여행 날짜

}
