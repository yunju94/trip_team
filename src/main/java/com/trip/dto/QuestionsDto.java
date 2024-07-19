package com.trip.dto;

import com.trip.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionsDto {
    private Long id;
    private  String writer;
    private String title;
    private String content;
}
