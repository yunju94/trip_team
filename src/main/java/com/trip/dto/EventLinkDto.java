package com.trip.dto;

import com.trip.entity.EventLink;
import com.trip.entity.eventImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class EventLinkDto {
    private Long id;
    private  String link;


    private static ModelMapper modelMapper = new ModelMapper();
    public static EventLinkDto of(EventLink eventlink) {
        return  modelMapper.map(eventlink, EventLinkDto.class);}
}
