package com.trip.dto;

import com.trip.entity.Item;
import com.trip.entity.ItemImg;
import com.trip.entity.Member;
import com.trip.entity.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

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

    // -------------------------------------------------------
    // ItemImg
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); // 상품 이미지 정보

    private List<Long> itemImgIds = new ArrayList<>(); // 상품 이미지 아이디

    // -------------------------------------------------------
    // ModelMapper
    private static ModelMapper modelMapper = new ModelMapper();

    public Review createItem() {
        // ItemFormDto -> Item 연결
        return modelMapper.map(this,Review.class);
    }
    public static ReviewFormDto of(Review review) {
        // Item -> ItemFormDto 연결
        return modelMapper.map(review, ReviewFormDto.class);
    }

}
