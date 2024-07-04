package com.trip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnADto {
    private int id;
    private String category;
    private String title;
    private String content;
}
