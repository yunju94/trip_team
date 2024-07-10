package com.trip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private Long questionId;
    private String writer;
    private String content;
}
