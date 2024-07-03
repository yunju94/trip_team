package com.trip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
public class CartItem {// 찜 목록
    // public(공용) > protected(상속) > default(같은 패키지 내부) > private(클래스 안에서만)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//AI
    @Column(name = "cart_item_id")
    private  Long id;

    private  int count;

    @ManyToOne(fetch = FetchType.LAZY)
    private  Cart cart;


}
