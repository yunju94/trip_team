package com.trip.entity;

import com.trip.constant.ItemSellStatus;
import com.trip.dto.ItemFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Member;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //상품코드

    @Column(nullable = false, length = 50)
    private String itemNm; // 상품명

    @Column(name = "price", nullable = false)
    private int price; //가격

    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태


    private String nature; // 국내, 해외
    private String category; // 미국 일본..
    private int stockNumber; // 인원 수
    private String departuredate;//출발일
    private String arrivaldate;//도착일


    @ManyToMany
    @JoinTable(
            name = "member_item",
            joinColumns=@JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    //private List<Member> member;

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public  void  cancelAdditem(int stockNumber){
        this.stockNumber += stockNumber;
        //인원수를 다시 받아서 증가시킨다.
    }

}