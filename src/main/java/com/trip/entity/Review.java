package com.trip.entity;

import com.trip.dto.ReviewFormDto;
import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "review")
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private  Item item;// 아이템 이름 필요

    @Column(nullable = false,columnDefinition = "TEXT")
    private  String content;//내용

    @Column(nullable = false,columnDefinition = "TEXT")
    private  String title;// 소제목


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private  Member member;

    private int Star;//별점


    public static Review  saveReview(ReviewFormDto reviewFormDto){
        Review review = new Review();
        review.setTitle(reviewFormDto.getTitle());
        review.setContent(reviewFormDto.getContent());
        review.setMember(reviewFormDto.getMember());
        review.setItem(reviewFormDto.getItem());
        review.setStar(reviewFormDto.getStar());
        return review;
    }


}
