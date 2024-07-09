package com.trip.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemDto {
    private Long id;
    private String itemNm;
    private String itemDetail;
    private Integer price;
    private String sellStatCd;
    private String category;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String nature;
}
