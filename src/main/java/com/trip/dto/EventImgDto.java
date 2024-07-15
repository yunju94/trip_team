package com.trip.dto;


import com.trip.entity.eventImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class EventImgDto {
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    private static ModelMapper modelMapper = new ModelMapper();
    public static EventImgDto of(eventImg eventImg) {
        return  modelMapper.map(eventImg, EventImgDto.class);}
}
