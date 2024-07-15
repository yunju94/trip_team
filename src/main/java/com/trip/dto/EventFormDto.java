package com.trip.dto;


import com.trip.entity.Item;
import com.trip.entity.event;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;



@Getter
@Setter
public class EventFormDto {
    private Long id;
    private String content;
    private String content1;
    private String content2;
    private String content3;

    private List<EventImgDto> eventImgDtoList = new ArrayList<>(); // 상품 이미지 정보

    private List<Long> eventImgIds = new ArrayList<>(); // 상품 이미지 아이디

    private static ModelMapper modelMapper = new ModelMapper();

    public event createEventTem(){
        // ItemFormDto -> Item 연결
        return modelMapper.map(this,event.class);
    }

    public static EventFormDto of(event event) {
        // Item -> ItemFormDto 연결
        return modelMapper.map(event, EventFormDto.class);
    }



}
