package com.trip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue
    private Long id; //상품코드
    private String nature; // 국내, 해외
    private String category; // 미국 일본..
    private String itemNm; // 상품명
    private int price; // 가격
    private int stockNumber; // 인원 수
    private String itemDetail; // 상세설명
    private String itemSellStatus; // 판매현황
}
