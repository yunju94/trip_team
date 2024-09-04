package com.trip.dto;

import com.trip.entity.Item;
import com.trip.entity.ItemImg;
import com.trip.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReviewFormDto {

    private Long id;

    private Item item;// 아이템 이름 필요

    private  String title;// 소제목

    private  String content;//내용

    private Member member;

    private int star;//별점

}
